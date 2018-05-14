package core.android.xuele.net.crhlibcore.uti;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * log日志相关<br>
 * Create 2014-5-28<br>
 * fixed by louweijun on 2016-8-29.<br>
 * Version 1.1.0
 */
public class LogManager {
	private final static int RETURN_DISABLE_LOG = -10; // Log被disable,将不输出信息
	private static final String DEFAULT_TAG = "TAG";

	private final static boolean USE_LOG = true;// true有log输出

	private LogManager() {
	}

	/**
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int v(String tag, String msg) {
		if (USE_LOG) {
			return android.util.Log.v(tag, msg);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int v(String tag, String msg, Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.v(tag, msg, tr);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int d(String tag, String msg) {
		if (USE_LOG) {
			return android.util.Log.d(tag, msg);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int d(String tag, String msg, Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.d(tag, msg, tr);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int i(String tag, String msg) {
		if (USE_LOG) {
			return android.util.Log.i(tag, msg);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int i(String tag, String msg, Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.i(tag, msg, tr);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int w(String tag, String msg) {
		if (USE_LOG) {
			return android.util.Log.w(tag, msg);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int w(String tag, String msg, Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.w(tag, msg, tr);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/*
	 * Send a {@link #WARN} log message and log the exception.
	 *
	 * @param tag Used to identify the source of a log message. It usually
	 * identifies the class or activity where the log call occurs.
	 *
	 * @param tr An exception to log
	 */
	public static int w(String tag, Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.w(tag, tr);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static int e(String tag, String msg) {
		if (USE_LOG) {
			return android.util.Log.e(tag, msg);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	public static int e(String tag, Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.e(tag, getStackTraceString(tr));
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	public static int e(Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.e(DEFAULT_TAG, getStackTraceString(tr));
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 *
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static int e(String tag, String msg, Throwable tr) {
		if (USE_LOG) {
			return android.util.Log.e(tag, msg, tr);
		} else {
			return RETURN_DISABLE_LOG;
		}
	}

	/**
	 * Handy function to get a loggable stack trace from a Throwable
	 *
	 * @param tr
	 *            An exception to log
	 */
	public static String getStackTraceString(Throwable tr) {
		if (USE_LOG) {
			if (tr == null) {
				return "";
			}
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			tr.printStackTrace(pw);
			return sw.toString();
		} else {
			return "";
		}

	}

	public static int d(String msg) {
		return d(DEFAULT_TAG, msg);
	}


	public static int d(String msg, Throwable t) {
        return d(DEFAULT_TAG, msg, t);
    }

    public static int e(String msg) {
        return e(DEFAULT_TAG, msg);
    }
}
