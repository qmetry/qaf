/**
 * 
 */
package com.qmetry.qaf.automation.report;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;
import static com.qmetry.qaf.automation.report.JsonReporter.getSuiteStatusCounters;
import static com.qmetry.qaf.automation.report.JsonReporter.getTestSetStatusCounters;
import static com.qmetry.qaf.automation.util.JSONUtil.getJsonObjectFromFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;

import com.qmetry.qaf.automation.integration.TestCaseResultUpdator;
import com.qmetry.qaf.automation.integration.TestCaseRunResult;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.util.DateUtil;
import com.qmetry.qaf.automation.util.FileUtil;

import jakarta.activation.DataHandler;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

/**
 * {@link TestCaseResultUpdator} implementation to generate emailable version of
 * <a href=
 * "https://qmetry.github.io/qaf/latest/qaf_reporting.html#overview">Overview
 * </a> with link to the detailed report.
 * <h3>Properties used</h3>
 * <dl>
 * <dt>emailable.report.enabled</dt>
 * <dd>boolean to enable/disable this listener. Default value is true.</dd>
 * <dt>report.server</dt>
 * <dd>set report server URL where actual detailed reports available. For
 * example jenkins job workspace URL.</dd>
 * <dt>emailablereport.savelocal</dt>
 * <dd>boolean to save email content in file. Default value is true.You use the generated file to send as email in CI post build action</dd>
 * <dt>emailablereport.sendemail</dt>
 * <dd>boolean to send email. Default value is false. You need to set smtp
 * properties, email credentials and recipient list to send email.</dd>
 * <dt>emailablereport.multipart</dt>
 * <dd>boolean to send email as multipart. Default value is false. Takes effect
 * if you configured to send email</dd>
 * <dt>emailablereport.user</dt>
 * <dd>sender email id. Required if you configured to send email</dd>
 * <dt>emailablereport.password</dt>
 * <dd>sender psasword. Required if you configured to send email</dd>
 * <dt>emailablereport.to</dt>
 * <dd>list of recipients email address. Required if you configured to send
 * email</dd>
 * <dt>emailablereport.subject</dt>
 * <dd>Optional email subject line. Will be used if you configured to send
 * email</dd>
 * </dl>
 * <h3>Features</h3> Generates emailable report same as <a href=
 * "https://qmetry.github.io/qaf/latest/qaf_reporting.html#overview">Overview
 * </a> in detailed reporting.
 * <ul>
 * <li>Execution summary with pass, fail, skip and total test count, percentage
 * </li>
 * <li>Pie chart or stacked bar chart</li>
 * <li>Test set wise pass, fail, skip and total test count and pass rate</li>
 * <li>Environment information</li>
 * <li>Link to dashboard for detailed report</li>
 * <li>Responsive design</li>
 * 
 * </ul>
 * <p>
 * If mail client doesn't supports advance CSS features, as a alternate of pie
 * chart, you may see horizontal stacked bar chart in email message. If
 * <code>app-logo.png</code> file is available at project root, it will be
 * placed in message header.
 * </p>
 * 
 * @author chirag.jayswal
 *
 */
public class EmailableReport implements TestCaseResultUpdator {

	private final String TEST_RESULT = "<tr>"
			+ "<td><a href=\"${report_server}/dashboard.htm#${suite_st_time}#${testname}\">${testname}</a></td>"
			+ "<td title=\"${test_st_end_time}\">${duration}</td>" + "<td>${pass}</td>" + "<td>${skip}</td>"
			+ "<td>${fail}</td>" + "<td>${total}</td>"
			+ "<td><div class=\"passrate_col\" style=\"background-color: #b6b6b6 !important;width: 100%;\"><span style=\"width:${passrate}%;color: #ffffff !important;background-color: #23a347;text-align: center !important;display: block;\">${passrate}%</span></div></td>"
			+ "</tr>";

	@Override
	public boolean updateResult(TestCaseRunResult result) {
		// do nothing
		return true;
	}

	@Override
	public String getToolName() {
		return "QAF Emailable Report";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforeShutDown() {
		List<StatusCounter> tests = getTestSetStatusCounters();

		StringBuffer testOverviews = new StringBuffer();
		int pass = 0, fail = 0, skip = 0;
		long endTime = 0;
		long stTime = getBundle().getLong("execution.start.ts");
		File reportDir = getReportDir();
		String report_server = getBundle().getString("report.server",
				FileUtil.getRelativePath(new File("."), reportDir));
		String host = System.getProperty("host.name");
		StringBuffer envInfo = new StringBuffer();

		for (StatusCounter test : tests) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("testname", test.getName());
			params.put("pass", test.getPass());
			params.put("fail", test.getFail());
			params.put("skip", test.getSkip());
			params.put("total", test.getTotal());
			params.put("passrate", test.getPassRate());
			params.put("suite_st_time", stTime);
			params.put("report_server", report_server);

			pass += test.getPass();
			fail += test.getFail();
			skip += test.getSkip();

			File overviewFile = new File(test.toString(), "overview.json");
			if (overviewFile.exists()) {
				TestOverview testOverview = getJsonObjectFromFile(test.toString() + "/overview.json",
						TestOverview.class);

				params.put("duration", getDuration(testOverview.getEndTime() - testOverview.getStartTime()));
				String test_st_end_time = DateUtil.getFormatedDate(new Date(testOverview.getStartTime()),
						"dd-MM-yy HH:mm") + " - "
						+ DateUtil.getFormatedDate(new Date(testOverview.getEndTime()), "dd-MM-yy HH:mm");
				params.put("test_st_end_time", test_st_end_time);

				endTime = Math.max(endTime, testOverview.getEndTime());

				Map<String, Object> runParameters = (Map<String, Object>) testOverview.getEnvInfo()
						.get("run-parameters");
				Map<String, Object> capabilities = (Map<String, Object>) testOverview.getEnvInfo()
						.get("browser-actual-capabilities");
				if (null == capabilities) {
					capabilities = (Map<String, Object>) testOverview.getEnvInfo().get("browser-desired-capabilities");
				}

				if (null != runParameters) {
					StringBuffer env = new StringBuffer();
					env.append("<br/><fieldset><legend>");
					env.append(test.getName());
					env.append(
							"</legend><div style=\"max-width: 1200px;margin: 0 auto;display: grid;gap: 1rem;grid-template-columns: repeat(auto-fit, minmax(10px, 1fr));\"><div><h5 style=\"display: block;margin: 0px;font: bold 18px/34px Arial, Helvetica, sans-serif;overflow-wrap: normal;\">Run-Parameters</h5>");
					runParameters.entrySet().forEach(entry -> {
						env.append("<b>" + entry.getKey() + "</b>: " + entry.getValue() + "<br/>");
					});
					if (null != capabilities) {
						env.append(
								"</div><div><h5 style=\"display: block;margin: 0px;font: bold 18px/34px Arial, Helvetica, sans-serif;overflow-wrap: normal;\">Capabilities</h5>");
						capabilities.entrySet().forEach(entry -> {
							env.append("<b>" + entry.getKey() + "</b>: " + entry.getValue() + "<br/>");
						});
					}
					env.append("</div></div></fieldset>");

					envInfo.append(env);
				}

			}

			testOverviews.append(StrSubstitutor.replace(TEST_RESULT, params));
		}
		int total = pass + fail + skip;

		if (testOverviews.length() > 0 && total > 0) {
			if (endTime <= 0) {
				endTime = System.currentTimeMillis();
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("suite_passed", pass);
			params.put("suite_failed", fail);
			params.put("suite_skipped", skip);
			params.put("suite_total", total);
			long passPer = Math.round(100.0 * pass / total);
			params.put("suite_passed_per", passPer);
			params.put("suite_failed_per", Math.round(100.0 * fail / total));
			params.put("suite_skipped_per", Math.round(100.0 * skip / total));

			System.out.println(params);
			params.put("pie_gradient", getPieGradient(pass, fail, total));
			params.put("suite_duration", getDuration(endTime - stTime));

			String suite_st_end_time = DateUtil.getFormatedDate(new Date(stTime), "dd-MM-yy HH:mm") + " - "
					+ DateUtil.getFormatedDate(new Date(endTime), "dd-MM-yy HH:mm");

			params.put("suite_st_end_time", suite_st_end_time);
			// collect(Collectors.joining(", ", "{", "}")

			String suiteName = getSuiteStatusCounters().stream().map(StatusCounter::getName)
					.collect(Collectors.joining("|"));
			params.put("suite_name", suiteName);
			params.put("test_results", testOverviews);
			params.put("suite_st_time", stTime);
			params.put("suite_st_time_formatted", DateUtil.getFormatedDate(new Date(stTime), "E MMM-dd-yy hh:mma"));
			params.put("host", host);

			params.put("env_info", envInfo);
			params.put("report_server", report_server);

			String qafLogoBase64 = "iVBORw0KGgoAAAANSUhEUgAAAN8AAAA/CAYAAABgvR2uAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAA/OSURBVHhe7Z17jF1FHcdN5NGCJEQSa4JEg/ISCqUPilrpbtt9tN0+drugCcUGDBZCQlSwFaOlRIhamkCpVIo8FlKixZRXW4yoof5RY2tiirHwB5ryB1EkIQoWQ9RkvJ+7+7v7u7+d85hzz7l3W2aST/aemTkzc878vmd+M3Pu3Q988JweF4lE2k8UXyTSIaL4IpEOEcUXibSRM1csbnyO4otE2sSZKxe74T3DjeMovkikDZy1aom7cu+wm/3t5Y24KL5IpGI+8YVR4S16YNCdeP54fBRfJFIhZ1+91F31/LBbvnOVO+XS3qa0KL5IpCI+tWZUeMO7h90Z3f0T0qP4IpEKOPfLNeH9fLjOx2tupy9P6eI7a2iJm7l+meu+b2WduRuX14feKRc3D7mRyPHKBWsHGsKbccsybx4oTXys5gw8MdSo1DL41Cp30c0DUYSR4xpsXGy+a8tKd8K5/nzQsvjO6Oqvj3BaaGkgUEZCX1mRyLHMjG8sa9j50h1DmQNNYfFRMC6lFlYICPajS8Z3+yORY5lZt41rYeiZVe70z/V582kKiY+hFTdSi6koCPi0udkNjUQmJTW3cs531CD0/HDTK2RpBImPkSptXleUOB+MHJPUhDf3jmbv78KbBvx5PeQSHyNTyLyuKHE+GCmD0y4fZ8rF/jytcsJ5Pe4zd61ost95P1jhzZtEqvgYidg20BW0gzgfjLTCtKW9bsXe/gZTL/XnKwrCQ2jaZvsfGXQnfTrMc0sUHyNQWfO6ojAfrNoV/WTXanftuk1u432Pu5Fdv3D7fneoDp+JG7zxdnfGrEHvuUksWH1r/VyYufwGb54i0FYplzrS0luFsnTZXEf31beUir6voW2nzygj6f6eu3ZcgPMf6HOnzJyYpwi8m3nF5mYvcOWuVe5Dl4WvW0wQH8be+9BgU+GdhAcAe4i2na1y3fq73aGX/+zyhmd+uX+CQSZxR804JBx5/Q1vniL85sBLY6W6eh02HUGWFay4dd1lBV1HK23nHiNI3V6Yc2dfQ4Bd2/vcqbOa00M58YIe13VPs/B4YXpaXzEvrUl87RbexgO3uydf3ekOv/WnOnuP7HH3//GH3rxnry5nLsiTkpHNhpde+UvdwDDqLSNP1T+/VutUG+59dFfmSKjFR/AJJZSv3/WjsdJGQ5b45HqKYkcUuSdpyP365ztHvekWXYduuy+vhTpsQIS6TOZ7ix4bdz+7H6wJcM74NYWAS+lb9+A1Ml/+PDSJz/qxVbFu/63uyNtHxm7ZxPDmv990d/9h04Tz2NDX7Q2FjvnH2/8aq8XVjYURME1MjHYYuhYiI2baKGjFR7DGHAJ16XYTssSnR5V2IdeNOHzpaei2+9KT4L4+W/NKJHCfdN9MW9I8/+v+cV/NRZxYThonX9TrFm6bOCix0unLn5eG+DBsW3gVIKqj/znq3v3vu27klUfdTftubKRd+6s1bltt5EN8BD7rc3ny6MaHQOdqA2Yk8eVLAoHy9JeAAJNEq8UnoiW/L28eMGYJUl4UXzM8RCXg2eg0Pf+DBQ/VBDi3+fwkpkzvdT3bJwrPfjevCA3x2f2KKmDEQ3ivvXOkSXQWREgegh0Bi4x+iASXhIC70oph6k5mHujLo8WnjconmCyGbrx97OzR80WIUXwT0Q9He/1zvjs+/6sL8OH++laEzmOZeol/Gub7bl4RGuJrx1zv8FuH6yOeHe1GXn6kPvfT8z3iGQFB4mD6zclviSfx2FMvjHWJqxuzL08Iev7lM3ItPo61UYS4nzw0ZLRmDkdcFF8y3C8J9LlOO3l6bf43Mi4+WPhIsgARHtsH2vYg6bt5RWiIz1ZSNgiO8LNXn2zEITA990OYB9840EjH7STo0S9UfPj/EnwGWxTEQLAuDljxYRTiLvryJ6HnMiLaKL505P74Vpk/ouZ/y3f3uSW7et28rT0TXNCpM/zCg6Tv5hWhbeJjfkfA9bRxBNxMVj8JMtdDnIS9r+1tnBMqPjEI3M2kOVoRtLHY0cyKz+bPM9+07qbER/Glo70MX/o5X+l1S5/uc/07e92iHT1u/sML3azvL3CnXraonn7KzF63+DH/K5SXrgv3utJoWXyIiS0C3+oloxguJaMeo5d1IcUN3ff6i3XhSRl6dCSdfHIcKj4ZoegUX3oryHK3FYJPfCCGYVfkLD53U4jiS0dPMXzpMHvDYtc9ssh9/sGF7rP3L3Bzt3S76Ru63YcX1oS5wy+8rO/mFaGw+BALe3N5A3M6u8jCiMYCDMJEgBL0KiehqPj0HKCMuZ5FOtquZCaJL6/7mTZHjOJLR+6PfWhpTrqwx11x71J3+dZuN+eeLjdz03w3e/MCN/i039Vc9sRwJW9aFRKfzMUkyOjFiIUoEROff//3g2M5RgNC024nbiXn6oD7qdMJRd3Osjo0iSSRJcWDdid97qdus09gUXzJ6IdtlqdzeteimuC63IzvIbxuN/Ssf8S7cvdVub6bV4Rg8ekRCjfS7sVZEBDikeAToAiVOSDHkiZzQgQtcSHi04buS2+VpPLTxAeykGLdT4xHtkQYIfU5Ql7xybuprcLWiq0niU6LT7uceR4+569ZMSq855K/Jpf3u3lFCBKfHvFwBbVQskBwMspZAVpIw0UlXytbDVkiaBVtMAhH4rPqJa/MF7X7qd3NJOPJK76yQoiQOik+fc/tNkMaKx7/YpN9aWbfOuQ9pyxyi0+2CgjaNQxBC5D5oi8PMLoiOoIVaYj49Ia4L71Vkgwmj+j1qMln5nYS0lymvOJjzkPeVsly3zR5xcfDx37D4Wt3bqufS7BpaTA6i7dA4Lr1gzALthVWPddsf7DywS9585dJbvGJu4l47IiHQBATq5USh0CJs+JhPighyWXFDSVQp02bTHO+JHHnER9o91O+YYG7mWY8GDYhS3x8tulVk1d8up1lBh4UIcITLrm+Nrd7ftzG1uy+Lvi7eUXIJT496ultAJtGYNsBJOA6WrHishK0WDWUyTxPz/WEEPHp0cSuGpaBuIl2fpZXfNr9lJAlmii+8cB9py7qTdu6ycPqx2+o29c1L1ztPtZb3n5wGrnEpzfDffM8Gal8wYoV9NwRodn0NELEB2LcoS9S50H2EO0cI6/4QLufeVy840F8Pmzbk9CLKiGLQVnQ9m/t/6b77d/87+tWQS7xyUjFiqQvXbYUEKnEiSB94kPAErJWSy2h4hPXLuS1rjzo19asEYSID2hjlrspvB/E50sXuEfy0MNdL8ujoe3v/e+9erm+9CoIEp8VEvM/WRjRe3GCiNLOB0HO84kzjVDx6ZGlTIOUJ7BvSyBUfCG838UHVQiwyj5LIpf4JFih6OCbn8noR9BvqUCSoLMIFR8gEEIr36nTaEPxiSCKr1rxAQKUfi1DgJNOfLiHGw9sGGvS6AjGa2Iy79PBJyK9ud5J8enRj81nX5680OmytM3T15cniq968QGCkzk9Aszjticx6cSn9+UkaLGwWIIQEZLdOGckJMh2gV1YkdAO8YGsTBKKCpDOli0BQtKEP4qvPeIDLUD6pqgAJ534AHHJt8p9+26gRzjmdvLtBIL+Lp6ASCXYfcAsiooP9EoZHZXXQOlQNnN5uuqQ5O5E8bVPfKDPLSrAjoqvL+Wb7AjQt6ACiIdRTwJClcUUgm8vT8TKqGrTsmhFfKBvMoHO4hfJ+H1O3pig4xAUn/ltSEZJLTrmGYx42t2xApwM4uONEf0mSChF9s06JT7QLzzQp748aXRUfK38hgtbDAhJXjtDrByDdSv1pnySoNNo9RfMgI4WI84bEJt+gwLB6RU3/bSdDOJrNfjKz6KT4gMtwNCpRUfF1+qvlyE4Pa9LcifFhUWYnOPLk0T31uK/XuYDAXHTk4TIKIeryga9z5UhDgG2us8XwvEqPvqC84qcqyk6t6+yz5JoiA/mbar2dzvl/VBC6OY6lDHqRSKThSbx8W3dKv4FGCOiXoRJWrhJo6xfrI5EJgtN4gMEmLb4EgIrnS+q0Y4QOs/jfzVE4UWORyaIT7hm823uwF8P1jfWQ9AjnA7M8UJdTdzgsv5rLXM3O49hnmHna8ybyMdqH3+Bdy/ls+Tjs8xR+PFcveLJvIONfTkGyvW9OM28kjQd56uX9swYWOu2/2RPIx9x1C3tYDtE0mgPaXau6msD94B4qQtoE2VIG0jn/Vh7XUDd9j5yzCqyjqMt+oeG9fUJxFOXbjd12hfj9XVwXlpfSNnUxzxQyiZN56XN9jqqJFF8cF7/NW7PoV+PyadYYIGFFc+QxRVG3jL/Px83mxsPulNFaDqvL45O1cd0mjYGOhARyfI8+VmIkbr4y7Eth46mLh4MOl6w+bX4qIt0qRMoR4yS66BOrlnSwZYJtME+AIA4nZ/rsHWSh3qpS+KAMonT5dI2Vo3l2NcW4N5qEXANunzKlOvM0xeSBuSVc0mT9lFfUj9URar4hOVfXedGDvy0LqK88IaLfasli6pcTDEubrDuKOKs0HxxugPF6HU6ULbtVDF84nl62/M4xqDpdG3QOl0fa/FJmTodxPDkOkBfs6/tcn9sPHE2P/Xq0ULabsvgWK6ZaySNc3V5vrYA5cm9k/sDUq/UKQ8DfS7YvtBpnMf5kka7KFfyt5Nc4hMQRhULMsDmeRU/zwb6qak7gxuPkchxUlzWORIv+eQvHYrrpTtb55dy+Cx5NDo/aPHZNAGjpTxdPnk5ls86P5CPjWncSoF4zuE9VvkxJa5FG6k2ZD7rsilT2kEe0qxYeFCQT5A2guRHSIgYrKDIz3lyjkC85OGvtB84FhHzmQcT9kE9uox2ECQ+QCAIpaz/WlvmvM4HNxVjkpuPkcmN9nUenaFHCpCOBG1wGp9xiLHxV8fLZ4xZ2oWRSz6dRx/bkU8bq4Ah0UZ9bZQr8bZMsIYvEMe18pd7Yl1Y0phD6WugDtJ0mbqt9h7IZwt9BPpecw3cZ+kf6rJtAvLIedRB3cBnPWpzrPPptHYQLD4BwbSyL1j2vC4JffN1J5AmRilGz1+EKgaky7DHuqM4jzg5z+YXJF6MRrcJY7UPAluOnfPptgPGKsYoZUoa7aU8X9vSxKfLQERi+NRrr0GLJalMXb+vLYLcUy0+6ufhqfuHNiA2OU7rC+I4lnum04iz97NqCotPQEAhWxOMmOddP+Atq2zoFGvQoJ/EGAw3HdHRGbojBd1JQAdhFMTLaqN9our8Np5ztQEJtEMf23K0+IBrkDbQfq5LjIc0e+1cq69tecUHnM+1Ep90r7i2pDJ1/QiHfBqd1wpLHiA6j+0L7mFaX1CeCNqm+cqvkpbFJyCoLFd05vrq5nWRyLFGaeITWJThJW3ewwRcU4QZRReJNFO6+CKRSD6i+CKRDhHFF4l0iCi+SKRDRPFFIh0iii8S6Qg97v+LWex3GSwg0wAAAABJRU5ErkJggg==";
			String appLogoBase64 = qafLogoBase64;

			File appLogo = new File("app-logo.png");
			if (appLogo.exists()) {
				try {
					appLogoBase64 = FileUtil.getBase64String(appLogo);
				} catch (IOException e) {
				}
			}
			params.put("app-logo", "data:image/png;base64," + appLogoBase64);
			params.put("qaf-logo", "data:image/png;base64," + qafLogoBase64);

			try (InputStream inputStream = getClass().getResourceAsStream("emailablereport_tmpl-inline.htm")) {
				String templ = IOUtils.toString(inputStream, "utf-8");
				String report = StrSubstitutor.replace(templ, params);

				//write to file for reference
				if (getBundle().getBoolean("emailablereport.savelocal", true)) {
					FileUtil.write(new File(reportDir, "qaf-emailable-report.htm"), report, "utf-8");
				}
				if (getBundle().getBoolean("emailablereport.sendemail", false)) {
					String subLine = String.format("%s completed with %d%% pass rate", suiteName, passPer);

					if (getBundle().getBoolean("emailablereport.multipart", false)) {
						params.put("qaf-logo", "cid:qafLogo");
						if (!appLogo.exists()) {
							appLogoBase64 = null;
							params.put("app-logo", "cid:qafLogo");
						} else {
							params.put("app-logo", "cid:appLogo");

						}
						report = StrSubstitutor.replace(templ, params);
						sendEmail(report, subLine, appLogoBase64, qafLogoBase64);
					} else {
						sendEmail(report, subLine, null, null);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File getReportDir() {
		List<StatusCounter> suiteCounters = getSuiteStatusCounters();
		File f = null;

		if (null != suiteCounters && suiteCounters.isEmpty()) {
			f = new File(suiteCounters.get(0).toString()).getParentFile();
		} else {
			f = new File(ApplicationProperties.JSON_REPORT_DIR.getStringVal("."));
		}
		if (f.getName().equalsIgnoreCase("json")) {
			return f.getParentFile();
		}
		return f;
	}

	private static String getDuration(long ms) {
		if (ms < 0)
			return "N/A";
		Double secs = ms / 1000.0;
		Double hours = Math.floor(secs / (60 * 60));
		Double divisor_for_minutes = secs % (60 * 60);
		Double minutes = Math.floor(divisor_for_minutes / 60);
		Double divisor_for_seconds = divisor_for_minutes % 60;
		Double seconds = Math.ceil(divisor_for_seconds);
		return hours.intValue() + ":" + minutes.intValue() + ":" + seconds.intValue();
	}

	private static String getPieGradient(int pass, int fail, double total) {
		double pass_deg = 360 * (pass / total);
		double fail_deg = 360 * ((pass + fail) / total);
		// conic-gradient(#23a347 208deg, #e63c20 208deg 360deg, #f3b600 0deg)
		return String.format("conic-gradient(#23a347 %sdeg, #e63c20 %sdeg %sdeg, #f3b600 0deg)", pass_deg, pass_deg,
				fail_deg);
	}

	@Override
	public boolean enabled() {
		return getBundle().getBoolean("emailable.report.enabled", true);
	}

	private static void sendEmail(String content, String defSubject, String appLogo, String qafLogo) {
		Configuration config = getBundle().subset("emailablereport");
		Session session = getSesstion(config.getString("user"), config.getString("password"));

		try {
			String[] toList = config.getStringArray("to");

			InternetAddress[] toAddress = new InternetAddress[toList.length];

			for (int i = 0; i < toList.length; i++) {
				toAddress[i] = new InternetAddress(toList[i]);
			}
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(config.getString("user")));
			message.addRecipients(Message.RecipientType.TO, toAddress);
			message.setSubject(config.getString("subject", defSubject));

			if (null == qafLogo) {
				message.setContent(content, "text/html; charset=utf-8");
			} else {
				// multi-part
				Multipart multiPart = new MimeMultipart("alternative");

				MimeBodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent(content, "text/html; charset=utf-8");

				MimeBodyPart qafLogoPart = createImagePart(qafLogo, "qafLogo");
				multiPart.addBodyPart(qafLogoPart);

				if (null != appLogo) {
					MimeBodyPart appLogoPart = createImagePart(appLogo, "appLogo");
					multiPart.addBodyPart(appLogoPart);
				}

				multiPart.addBodyPart(htmlPart); // <-- second
				message.setContent(multiPart);

			}
			Transport.send(message);

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	private static MimeBodyPart createImagePart(String base64Data, String name) throws MessagingException {
		MimeBodyPart imagePart = new MimeBodyPart();
		ByteArrayDataSource imageDataSource = new ByteArrayDataSource(Base64.getDecoder().decode(base64Data),
				"image/png");

		imagePart.setDataHandler(new DataHandler(imageDataSource));
		imagePart.setHeader("Content-ID", "<" + name + ">");
		imagePart.setFileName(name + ".png");
		// imagePart.setDisposition("inline");

		return imagePart;
	}

	private static Session getSesstion(String user, String pwd) {
		Properties props = System.getProperties();

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pwd);
			}
		};
		Session session = Session.getDefaultInstance(props, auth);
		return session;
	}
}
