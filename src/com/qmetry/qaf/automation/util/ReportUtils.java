/**
 * 
 */
package com.qmetry.qaf.automation.util;

import java.util.List;

import com.qmetry.qaf.automation.core.CheckpointResultBean;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;

/**
 * @author chirag.jayswal
 *
 */
public class ReportUtils {

	/**
	 * static access only
	 */
	private ReportUtils() {
	}

	public static String getFailureMessage(Throwable t) {
		if (null == t)
			return "";
		String msg = t.getMessage();
		if (StringUtil.isNotBlank(msg))
			return msg;
		return t.toString();
	}
	
	public static void setScreenshot(Throwable t) {
		String failiremsg = ReportUtils.getFailureMessage(t);
		QAFTestBase stb = TestBaseProvider.instance().get();
		CheckpointResultBean lastFailedChkPoint =
				getLastFailedCheckpointResultBean(stb.getCheckPointResults());

		// not an assertion of verification failure
		if (null != lastFailedChkPoint) {
			// ensure last failed check-point has screenshot

			if (StringUtil.isBlank(lastFailedChkPoint.getScreenshot())) {
				// get last failed sub-checkpoint
				CheckpointResultBean lastFailedSubChkPoint =
						getLastFailedCheckpointResultBean(
								lastFailedChkPoint.getSubCheckPoints());

				if (lastFailedSubChkPoint != null && StringUtil
						.isNotBlank(lastFailedSubChkPoint.getScreenshot())) {
					lastFailedChkPoint
							.setScreenshot(lastFailedSubChkPoint.getScreenshot());

				} else {
					stb.takeScreenShot();
					lastFailedChkPoint.setScreenshot(stb.getLastCapturedScreenShot());
				}
			}
		} else if (StringUtil.isNotBlank(failiremsg)) {
			// stb.addAssertionLogWithScreenShot(failiremsg,
			// MessageTypes.Fail);
			stb.takeScreenShot();
			CheckpointResultBean stepResultBean = new CheckpointResultBean();
			stepResultBean.setMessage(failiremsg);
			stepResultBean.setType(MessageTypes.Fail);
			stepResultBean.setScreenshot(stb.getLastCapturedScreenShot());
			stb.getCheckPointResults().add(stepResultBean);

		}
	}
	
	public static CheckpointResultBean getLastFailedCheckpointResultBean(
			List<CheckpointResultBean> checkPointResults) {
		if ((null == checkPointResults) || checkPointResults.isEmpty()) {
			return null;
		}

		// There may be not run check point at the end. Visit form bottom, we
		// need to visit up to failure check point found.
		for (int index = checkPointResults.size() - 1; index >= 0; index--) {
			CheckpointResultBean checkpointResultBean = checkPointResults.get(index);
			MessageTypes type = MessageTypes.valueOf(checkpointResultBean.getType());
			if (type.isFailure())
				return checkpointResultBean;
		}

		return null;
	}
}
