package com.twister.utils;

public class Constants {
	public final static boolean isdebug = false;
	public final static int bufferSize = 1024;
	public final static int QueueSize = Integer.MAX_VALUE;
	// maxFrameLength：解码的帧的最大长度
	// stripDelimiter：解码时是否去掉分隔符
	// failFast：为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
	public final static int NumWorkers = 4;

	public final static int PullSpout = 40;
	public final static int ShuffleBolt = 40;
	public final static int FieldsGroupBolt = 120;
	public final static int StatisBolt = 120;

	// public final static int PullSpout = 4;
	// public final static int ShuffleBolt = 4;
	// public final static int FieldsGroupBolt = 6;
	// public final static int StatisBolt = 6;

	public final static int MaxFrameLength = 8192;
	public final static boolean FailFast = false;
	public final static boolean StripDelimiter = true;
	public static final long FREQUENCY = 1L;
	public final static long OutPutTime = 5 * 60 * 1000;
	public final static long SyncInterval = 1 * 60 * 1000; // 1分钟
	// nginxAccess
	public static String nginxAccess = "/opt/logs/nginx/access/log";
	// mongo table
	public static String SpoutTable = "twisterServer";
	public static String ApiStatisTable = "interface";
}
