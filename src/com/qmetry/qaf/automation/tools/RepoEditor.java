/*******************************************************************************
 * Copyright (c) 2019 Infostretch Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.qmetry.qaf.automation.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.impl.bootstrap.HttpServer;
import org.apache.hc.core5.http.impl.bootstrap.ServerBootstrap;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.step.StepNotFoundException;
import com.qmetry.qaf.automation.step.StringTestStep;
import com.qmetry.qaf.automation.util.FileUtil;
import com.qmetry.qaf.automation.util.JSONUtil;
import com.qmetry.qaf.automation.util.StringUtil;
import com.qmetry.qaf.automation.ws.Response;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;

/**
 * @author chirag.jayswal
 *
 */
public class RepoEditor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = ConfigurationManager.getBundle().getInt("repoeditor.server.port",2612);
		final HttpServer server = createServer(port);
		try {
			server.start();
			System.out.println("server started on port: " + port);
			System.out.println("type \"exit\" to stop server.");

			try (Scanner s = new Scanner(System.in)) {
				s.next("exit");
			}
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static HttpServer createServer(int port) {
			HttpServer server =  ServerBootstrap.bootstrap().setListenerPort(port).register("/", (req, res, c) -> {
				res.setEntity(new FileEntity(new File("dashboard.htm"), ContentType.TEXT_HTML));
			}).register("/browse", (req, res, c) -> {
				res.setEntity(new StringEntity(browse("."), ContentType.TEXT_HTML));
			}).register("/executeRequest", (req, res, ctx) -> {
				try {
					String reqCall = IOUtils.toString(req.getEntity().getContent(), StandardCharsets.UTF_8);
					Object[] args = JSONUtil.toObject(reqCall, Object[].class);
					StringTestStep.execute("userRequestsWithData", args);
					Response result = new RestTestBase().getResponse();
					String resStr = new JSONObject(result).toString();
					res.setEntity(new StringEntity(resStr, ContentType.APPLICATION_JSON));
				} catch (StepNotFoundException se) {
					res.setEntity(new StringEntity("ERROR: Add qaf-support-ws library dependency!...",
							ContentType.TEXT_PLAIN));
				} catch (Throwable t) {
					t.printStackTrace();
					res.setEntity(new StringEntity(t.getMessage(), ContentType.TEXT_PLAIN));
				}
			}).register("/executeStep", (req, res, ctx) -> {
				Map<String, Object> responseToreturn = new HashMap<String, Object>();
				try {
					String reqCall = IOUtils.toString(req.getEntity().getContent(), StandardCharsets.UTF_8);
					Map<String, Object> reqMap = JSONUtil.toMap(reqCall);
					
					Object result = StringTestStep.execute(reqMap.get("step").toString(), ((List<Object>)reqMap.get("args")).toArray(new Object[] {}));
					responseToreturn.put("result", result);
				}catch (Throwable t) {
					t.printStackTrace();
					responseToreturn.put("error", t.getMessage());
				}
				res.setEntity(new StringEntity(JSONUtil.toString(responseToreturn), ContentType.APPLICATION_JSON));
			}).register("/repo-editor", (req, res, ctx) -> {
				try {
					Map<String, String> queryParams = getQueryParams(req);
					System.out.println(queryParams);

					if (!queryParams.isEmpty()) {
						switch (queryParams.getOrDefault("operation", "")) {
						case "get_prop":
							res.setEntity(new StringEntity(ConfigurationManager.getBundle().getString(queryParams.getOrDefault("name",""),""),
									ContentType.APPLICATION_JSON));
						case "get_node":
							res.setEntity(new StringEntity(JSONUtil.toString(getNodes(queryParams.get("id"))),
									ContentType.APPLICATION_JSON));
							break;
						case "get_wsc_content":
							Map<String, Object> wsc = getWSCNodeContent(queryParams.get("path"), queryParams.get("name"));
							if(wsc.containsKey("reference")) {
								wsc.put("refObj",JSONUtil.toObject(ConfigurationManager.getBundle().getString(wsc.get("reference").toString(),"{}")));
							}
							res.setEntity(new StringEntity(
									JSONUtil.toString(wsc ),
									ContentType.APPLICATION_JSON));
							break;
						case "get_grpc_content":
							try {
								Object result = StringTestStep.execute("getGRPCMethodDetails", queryParams.get("path"),queryParams.get("name"));
								res.setEntity(new StringEntity(JSONUtil.toString(result), ContentType.APPLICATION_JSON));
							} catch (StepNotFoundException se) {
								res.setEntity(new StringEntity("ERROR: Add qaf-support-grpc library dependency!...",
										ContentType.TEXT_PLAIN));
							} catch (Throwable t) {
								t.printStackTrace();
								res.setEntity(new StringEntity(t.getMessage(), ContentType.TEXT_PLAIN));
							}
							break;
						case "get_content":
							res.setEntity(new StringEntity(
										JSONUtil.toString(getContent(queryParams.get("path"))),
										ContentType.APPLICATION_JSON));
							break;
						case "save_wsc":
							// create file, create/rename request call
							String message = "Saved " + queryParams.get("path");
							String content = IOUtils.toString(req.getEntity().getContent(), StandardCharsets.UTF_8);
							saveWsc(JSONUtil.toMap(content), queryParams);
							res.setEntity(new StringEntity(message, ContentType.TEXT_PLAIN));
							break;
						case "save_loc":
							String data = IOUtils.toString(req.getEntity().getContent(), StandardCharsets.UTF_8);
							saveContent(JSONUtil.toObject(data, List.class), queryParams.get("path"));
							break;
						case "save_file":
							String fileContent = IOUtils.toString(req.getEntity().getContent(), StandardCharsets.UTF_8);
							FileUtil.writeStringToFile(new File( queryParams.get("path")), fileContent, ApplicationProperties.LOCALE_CHAR_ENCODING.getStringVal("UTF-8"));
							break;
						case "load_file":
							ConfigurationManager.getBundle().load(new String[] {queryParams.get("path")});
							break;
						case "move_node":
							res.setEntity(new StringEntity(JSONUtil.toString(moveNode(queryParams)),
									ContentType.APPLICATION_JSON));
							break;
						case "copy_node":
							res.setEntity(new StringEntity(JSONUtil.toString(copyNode(queryParams)),
									ContentType.APPLICATION_JSON));
							break;
						case "rename_node":
							res.setEntity(new StringEntity(JSONUtil.toString(rename(queryParams)),
									ContentType.APPLICATION_JSON));
							break;
						case "create_node":
							res.setEntity(new StringEntity(JSONUtil.toString(create(queryParams)),
									ContentType.APPLICATION_JSON));
							break;
						case "delete_node":
							delete(queryParams);
							break;
						default:
							break;
						}
					} else {
						res.setEntity(new FileEntity(new File("repo-editor.html"), ContentType.TEXT_HTML));
					}
				} catch (Exception e) {
					res.setCode(400);
					res.setEntity(new StringEntity(e.getMessage(), ContentType.TEXT_PLAIN));
				}
			}).register("*", (req, res, ctx) -> {
				String endpoint;
				try {
					endpoint = req.getPath().substring(1).split("\\?", 2)[0];
					endpoint = req.getUri().getPath().substring(1);
				} catch (URISyntaxException e1) {
					endpoint = req.getPath().substring(1).split("\\?", 2)[0];

				}
				File file = new File(endpoint);
				boolean exist = file.exists();
				switch (req.getMethod().toUpperCase()) {
				case "GET":
					if (exist) {
						if (file.isDirectory()) {
							res.setEntity(new StringEntity(browse(endpoint), ContentType.TEXT_HTML));
						} else {
							res.setEntity(new FileEntity(file, getContentType(endpoint)));
						}
					} else {
						res.setCode(404);
						res.setReasonPhrase("Not avialable");
						res.setEntity(new StringEntity(req.getPath() + " not avialable."));
					}
					break;
				default:
					break;
				}
			}).create();
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				System.out.println("stoping server!....");
				server.stop();
			}));
			return server;
	}

	private static String browse(String dir) {
		try {
			return Files.list(new File(dir).toPath())
					.map(p -> String.format("<a href=\"/%s\">%s</a>", p, p.toString().replace(dir, "")))
					.collect(Collectors.joining("<br>"));
		} catch (IOException e) {
			return e.getMessage();
		}
	}

	private static ContentType getContentType(final String filename) {
		try {
			if (filename.endsWith(".js")) {
				return ContentType.create("text/javascript", "UTF-8");
			}
			if (filename.endsWith(".css")) {
				return ContentType.create("text/css", "UTF-8");
			}
			if (filename.endsWith(".wsc")) {
				return ContentType.APPLICATION_JSON;
			}
			if (filename.endsWith(".loc")) {
				return ContentType.APPLICATION_JSON;
			}
			if (filename.endsWith(".png")) {
				return ContentType.IMAGE_PNG;
			}
			if (filename.endsWith(".svg")) {
				return ContentType.IMAGE_SVG;
			}
			return ContentType.create(Files.probeContentType(new File(filename).toPath()));
		} catch (Exception e) {
			return ContentType.TEXT_PLAIN;
		}
	}

	private static Map<String, String> getQueryParams(HttpRequest request) {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		try {
			String query = request.getUri().getQuery();
			String[] pairs = query.split("&");
			for (String pair : pairs) {
				try {
					String kv[] = pair.split("=", 2);
					query_pairs.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
				} catch (UnsupportedEncodingException | ArrayIndexOutOfBoundsException e) {
	
				}
			}
		} catch (Throwable e1) {
		}
		return query_pairs;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object>  getWSCNodeContent(String path, String name) {
		Map<String, Object> fileContent = getWSCFile(path);
		return (Map<String, Object>) fileContent.getOrDefault(name, Collections.emptyMap());
	}

	private static Map<String, String> copyNode(Map<String, String> queryParams) throws IOException {

		File parent = new File(queryParams.get("parent"));
		String id = queryParams.get("id");
		if (id.indexOf("#") > 0) {
			if (parent.getName().endsWith(".wsc")) {
				String[] parts = id.split("#", 2);

				// src
				Map<String, Object> fileContent = getWSCFile(parts[0]);
				Object wsc = fileContent.remove(parts[1]);

				// dest
				fileContent = getWSCFile(parent.getPath());
				fileContent.put(parts[1], wsc);
				saveWSCFile(fileContent, parent.getPath());

				id = queryParams.get("parent") + "#" + parts[1];
			} else {
				throw new IOException("Can't cop request call to" + parent.getName());
			}

		} else {
			File src = new File(id);
			File dest = new File(parent, src.getName());
			Files.copy(src.toPath(), dest.toPath());
			id = dest.getPath();
		}
		queryParams.clear();
		queryParams.put("id", id);
		return queryParams;
	}

	private static Map<String, String> moveNode(Map<String, String> queryParams) throws IOException {
		String nodeName = queryParams.get("name");
		File parent = new File(queryParams.get("parent"));
		File oldParent = new File(queryParams.get("oldParent"));
		String id = queryParams.get("parent") + "/" + nodeName;

		if (oldParent.isFile()) {
			if (parent.getName().endsWith(".wsc") || parent.getParent().contains(".wsc")) {
				if (parent.getParent().contains(".wsc")) {
					parent = parent.getParentFile();
				}
				// move wsc
				Map<String, Object> fileContent = getWSCFile(oldParent.getPath());
				Object wsc = fileContent.remove(nodeName);
				saveWSCFile(fileContent, oldParent.getPath());
				
				fileContent = getWSCFile(parent.getPath());
				fileContent.put(nodeName, wsc);
				saveWSCFile(fileContent, parent.getPath());
				
				id= parent.getPath() + "#" +nodeName;
			} else {
				throw new IOException("Can't move request call to" + parent.getName());
			}

		} else {
			// move file or folder
			System.out.println("moving file!....");
			Files.move(new File(oldParent, nodeName).toPath(), new File(parent, nodeName).toPath());
		}
		queryParams.clear();
		queryParams.put("id", id);
		return queryParams;
	}

	private static void delete(Map<String, String> queryParams) throws IOException {
		String id = queryParams.get("id").replace('#', '/');
		File target = new File(id);
		if (target.exists()) {
			FileUtil.deleteQuietly(target);
		} else if (target.getParent().endsWith(".wsc")) {
			Map<String, Object> fileContent = getWSCFile(target.getParent());
			fileContent.remove(target.getName());
			saveWSCFile(fileContent, target.getParent());
		}
	}

	private static void saveWsc(Map<String, Object> content, Map<String, String> queryParams) throws IOException {
		String path = queryParams.get("path");
		if (StringUtil.isNotBlank(queryParams.get("name"))) {
			Map<String, Object> fileContent = getWSCFile(path);
			fileContent.put(queryParams.get("name"), content);
			saveWSCFile(fileContent, path);
		}
	}

	private static Map<String, String> rename(Map<String, String> queryParams) throws IOException {
		String old = queryParams.get("old");
		String id = queryParams.get("id").replace('#', '/');
		String newName = queryParams.get("text");

		File file = new File(id);
		File parent = file.getParentFile();

		if (parent.getName().endsWith("wsc")) {
			file = parent;

			if (!file.exists()) {
				FileUtil.checkCreateDir(file.getParent());
			}
			Map<String, Object> fileContent = getWSCFile(file.getPath());

			if (StringUtil.isNotBlank(newName)) {
				Object val = fileContent.remove(queryParams.getOrDefault("old", ""));
				if (null == val) {
					val = JSONUtil.toObject("{}");
				}
				fileContent.put(newName, val);
			}
			saveWSCFile(fileContent, file.getPath());
			id=file.getPath()+"#"+newName;

		} else {
			if (StringUtil.isNotBlank(old)) {
				File oldFile = new File(parent, old);
				File newFile = new File(parent, newName);

				if(oldFile.exists()) {
				oldFile.renameTo(newFile);
				}else {
					FileUtil.checkCreateDir(parent.getParent());
				}
				id=newFile.getPath();
			}
		}
		queryParams.clear();
		queryParams.put("id", id);
		return queryParams;
	}
	private static Map<String, String> create(Map<String, String> queryParams) throws IOException {
		String type = queryParams.get("type");
		String id = queryParams.get("id");
		String name = queryParams.get("text");
		File parent = new File(queryParams.get("id"));
		if (parent.getName().endsWith("wsc")) {
			if (!parent.exists()) {
				FileUtil.checkCreateDir(parent.getParent());
			}
			Map<String, Object> fileContent = getWSCFile(parent.getPath());
			if (StringUtil.isNotBlank(name)) {
				name=name.replace(' ', '.');
				int cnt=2;
				if(ConfigurationManager.getBundle().containsKey(name)) {
					while(ConfigurationManager.getBundle().containsKey(name + cnt)) {cnt++;};
					name=name+cnt;
				}
				fileContent.put(name, JSONUtil.toObject("{}"));
				//ConfigurationManager.getBundle().setProperty(name,"");
				saveWSCFile(fileContent, parent.getPath());
			}
			id=id+"#"+name;
		} else {
			File file = new File(parent,name);
			if(type.equalsIgnoreCase("folder")) {
				if(file.exists()) {
					int cnt=2;
					while(new File(name + cnt).exists()) {cnt++;};
					name=name+cnt;
				}
				file = new File(parent,name);
				FileUtil.checkCreateDir(file.getPath());
			}else {
				file.createNewFile();
			}
			id=file.getPath();
		}
		queryParams.clear();
		queryParams.put("id", id);
		queryParams.put("text", name);

		return queryParams;
	}

	private static Object[] getNodes(String id) {
		try {
			if (id.equalsIgnoreCase("#")) {
				File tdir = new File("resources");
				boolean dirExists = tdir.exists();
				if (!dirExists) {
					dirExists = tdir.mkdirs();
					//FileUtil.
				}

				Map<String, Object> rootNode = new HashMap<String, Object>();
				rootNode.put("text", "resources");
				rootNode.put("id", "resources");
				rootNode.put("type", "folder");
				rootNode.put("children", getNodes("resources"));
				return new Object[] { rootNode };
			}
			File parent = new File(id);
			if (parent.getName().endsWith(".wsc")) {
				Map<String, Object> fileContent = new LinkedHashMap<String, Object>();
				if (parent.exists()) {
					fileContent = getWSCFile(parent.getPath());//JSONUtil.getJsonObjectFromFile(parent.getPath(), Map.class);
				}
				return fileContent.keySet().stream().map(k -> {
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("text", k);
					node.put("id", id + "#" + k);
					node.put("type", "node");
					return node;
				}).toArray();

			}
			if (parent.getName().endsWith(".proto")) {
				@SuppressWarnings("unchecked")
				List<String> result = (List<String>) StringTestStep.execute("getGRPCMethodNames", parent.getPath());
				System.err.println(result);
				System.out.println(parent);
				return result.stream().map(k -> {
					Map<String, Object> node = new HashMap<String, Object>();
					node.put("text", k);
					node.put("id", id + "#" + k);
					node.put("type", "node-grpc");
					return node;
				}).toArray();
			}
			//".wsc",".loc",".proto","json", "properties", "xml","txt", "csv"
			String[] allowedTypes = ConfigurationManager.getBundle().getStringArray("repoeditor.filetypes", ".wsc",".loc",".proto");
			Object[] nodes = Files.list(parent.toPath())
					.filter(p -> p.toFile().isDirectory() || StringUtil.endsWithAny(p.toString(), allowedTypes))
					.map(p -> {
						Map<String, Object> node = new HashMap<String, Object>();
						node.put("text", p.toFile().getName());
						node.put("id", p.toString());
						if (p.toFile().isDirectory()) {
							node.put("children", true);
							node.put("type", "folder");
						} else {
							String type = FileUtil.getExtention(p.toString());
							node.put("type", "file-"+type);
							node.put("children", "wscproto".indexOf(type)>=0);
						}
						return node;
					}).toArray();
			return (nodes);
		} catch (IOException e) {
			return new Object[0];
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Map<String, Object>> getContent(String file) {
		List<Map<String, Object>> fileContent = new LinkedList<Map<String, Object>>();

		try {
			PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
			propertiesConfiguration.setEncoding(ApplicationProperties.LOCALE_CHAR_ENCODING.getStringVal("UTF-8"));
			propertiesConfiguration.setDelimiterParsingDisabled(true);
			propertiesConfiguration.load(new FileInputStream(file));
			
			Iterator<String> iter = propertiesConfiguration.getKeys();
			if(file.endsWith(".wsc")) {
				Map<String, Object> contenct = new LinkedHashMap<>();
				while(iter.hasNext()) {
					String key = iter.next();
					String value = propertiesConfiguration.getString(key);
					contenct.put(key, JSONUtil.toObject(value));
				}
				fileContent.add(contenct);
			}
			if(file.endsWith(".loc")) {
				while(iter.hasNext()) {
					String key = iter.next();
					String value = propertiesConfiguration.getString(key);
					Object obj = JSONUtil.toObject(value);
					Map<String, Object> entry = new LinkedHashMap<>();
					entry.put("key", key);
					
					if(obj instanceof Map) {
						entry.putAll((Map<String, Object>)obj);
					}else {
						entry.put("locator", value);
					}
					fileContent.add(entry);
				}
			}
		} catch (ConfigurationException e) {
		} catch (FileNotFoundException e) {
		}
		return fileContent;
	}
	private static Map<String, Object> getWSCFile(String file){
		List<Map<String, Object>> content = getContent(file);
		return content.isEmpty()?JSONUtil.toMap("{}"):content.get(0);
	}
	
	private static void saveContent(List<Map<String, Object>> data, String file) throws IOException {
		Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
		String fileContent = null;
		if(file.endsWith("wsc")) {
			fileContent = data.get(0).entrySet().stream().map((e) -> e.getKey() +"=" + gson.toJson(e.getValue()).replace("\\", "\\\\")).collect(Collectors.joining("\n"));
		}else if(file.endsWith(".loc")) {
			fileContent = data.stream().map(e->e.remove("key").toString()+"="+gson.toJson(e)).collect(Collectors.joining("\n"));
		}
		
		if(null!=fileContent) {
			FileUtil.writeStringToFile(new File(file), fileContent, ApplicationProperties.LOCALE_CHAR_ENCODING.getStringVal("UTF-8"));
		}
		//to keep track added/updated wsc and properties
		ConfigurationManager.addBundle(file);
		
	}
	
	private static void saveWSCFile(Map<String, Object> data,String file) throws IOException{
		List<Map<String, Object>> fileContent = new LinkedList<Map<String, Object>>();
		fileContent.add(data);
		saveContent(fileContent,file);
	}
	
}
