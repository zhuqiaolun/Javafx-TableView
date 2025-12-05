package com.demon.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.LevelMatchFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * author: Demon
 * date: 2024-12-17 017 11:19
 * description: 日志类
 * // 日志级别：ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL
 * 两个配置项的三个属性值：
 * onMatch="ACCEPT" 表示匹配该级别及以上
 * onMatch="DENY" 表示不匹配该级别及以上
 * onMatch="NEUTRAL" 表示该级别及以上的，由下一个filter处理，如果当前是最后一个，则表示匹配该级别及以上
 * onMismatch="ACCEPT" 表示匹配该级别以下
 * onMismatch="NEUTRAL" 表示该级别及以下的，由下一个filter处理，如果当前是最后一个，则不匹配该级别以下的
 * onMismatch="DENY" 表示不匹配该级别以下的
 */
public class Log4j2Util {

    private boolean traceFlag;
    private boolean debugFlag;
    private boolean infoFlag;
    private boolean warnFlag;
    private boolean errorFlag;
    private boolean fatalFlag;
    private List<PackLoggerVo> packLoggerList;

    private Log4j2Util(boolean traceFlag, boolean debugFlag, boolean infoFlag, boolean warnFlag, boolean errorFlag, boolean fatalFlag, List<PackLoggerVo> packLoggerList) {
        this.traceFlag = traceFlag;
        this.debugFlag = debugFlag;
        this.infoFlag = infoFlag;
        this.warnFlag = warnFlag;
        this.errorFlag = errorFlag;
        this.fatalFlag = fatalFlag;
        this.packLoggerList = packLoggerList;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private static Appender getFileAppender(PatternLayout layout, String logRootPath, Level level, String appenderName) {
        //  日志文件名，根据日志级别取名
        String fileName = level.toString().toLowerCase();
        return RollingFileAppender.newBuilder().setName(appenderName)
                // 过滤日志文件内容
                .setFilter(LevelMatchFilter.newBuilder().setLevel(level).setOnMatch(Filter.Result.ACCEPT).setOnMismatch(Filter.Result.DENY).build())
                // 日志文件输出的文件名，每天滚动生成一个新的日志文件
                .withFileName(logRootPath + File.separator + fileName + ".log").withFilePattern(logRootPath + File.separator + "%d{yyyy-MM-dd}" + File.separator + fileName + ".logs-%i.log")
                // 设置日志输出格式
                .setLayout(layout)
                // 基于时间间隔的滚动策略，设置日志文件滚动更新的时间(1个小时)，依赖于文件命名filePattern的设置
                .withPolicy(TimeBasedTriggeringPolicy.newBuilder().withModulate(true).withInterval(1).build())
                // 基于文件大小的滚动策略, 设置日志基础文件大小，超过该大小就触发日志文件滚动更新，单位可为 KB, MB, GB，TB, 比如：10MB
                .withPolicy(SizeBasedTriggeringPolicy.createPolicy("10KB"))
                // 设置日志的文件个数上限，不设置默认为7个，超过大小后会被覆盖；依赖于filePattern中的%i
                .withStrategy(DefaultRolloverStrategy.newBuilder().withMax("30").build())
                .build();
    }

    public void init() {
        this.init(null);
    }
    /**
     * 加载日志
     *
     * @param logRootPath ，传 null 则不输出文件日志
     */
    public void init(String logRootPath) {
        Map<Level, AppenderRef> appenderRefMap = new LinkedHashMap<>();
        Map<Level, Appender> appenderMap = new LinkedHashMap<>();
        // 配置
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        // 输出格式
        final PatternLayout layout = PatternLayout.newBuilder().withCharset(Charset.forName("UTF-8")).withConfiguration(config).withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS,CTT}[%p][%t] %c[%L] %m %n").build();

        // 1.输出到控制台
        String consoleAppenderName = "CONSOLE_APPENDER";
        ConsoleAppender consoleAppender = ConsoleAppender.newBuilder().setName(consoleAppenderName).setLayout(layout).build();
        AppenderRef consoleAppenderRef = AppenderRef.createAppenderRef(consoleAppenderName, null, null);


        // 2.输出到文件，按照每天生成日志文件
        if (logRootPath != null) {
            // 日志输出级别
            if (traceFlag) {
                // TRACE
                String traceAppenderName = "TRACE_APPENDER";
                Appender traceAppender = getFileAppender(layout, logRootPath, Level.TRACE, traceAppenderName);
                appenderRefMap.put(Level.TRACE, AppenderRef.createAppenderRef(traceAppenderName, null, null));
                appenderMap.put(Level.TRACE, traceAppender);
            }
            if (debugFlag) {
                // TRACE
                String debugAppenderName = "DEBUG_APPENDER";
                Appender debugAppender = getFileAppender(layout, logRootPath, Level.DEBUG, debugAppenderName);
                appenderRefMap.put(Level.DEBUG, AppenderRef.createAppenderRef(debugAppenderName, null, null));
                appenderMap.put(Level.DEBUG, debugAppender);
            }
            if (infoFlag) {
                // INFO
                String infoAppenderName = "INFO_APPENDER";
                Appender infoAppender = getFileAppender(layout, logRootPath, Level.INFO, infoAppenderName);
                appenderRefMap.put(Level.INFO, AppenderRef.createAppenderRef(infoAppenderName, null, null));
                appenderMap.put(Level.INFO, infoAppender);
            }
            if (warnFlag) {
                // WARN
                String warnAppenderName = "WARN_APPENDER";
                Appender warnAppender = getFileAppender(layout, logRootPath, Level.WARN, warnAppenderName);
                appenderRefMap.put(Level.WARN, AppenderRef.createAppenderRef(warnAppenderName, null, null));
                appenderMap.put(Level.WARN, warnAppender);
            }
            if (errorFlag) {
                // ERROR
                String errorAppenderName = "ERROR_APPENDER";
                Appender errorAppender = getFileAppender(layout, logRootPath, Level.ERROR, errorAppenderName);
                appenderRefMap.put(Level.ERROR, AppenderRef.createAppenderRef(errorAppenderName, null, null));
                appenderMap.put(Level.ERROR, errorAppender);
            }
            if (fatalFlag) {
                // FATAL
                String fatalAppenderName = "FATAL_APPENDER";
                Appender fatalAppender = getFileAppender(layout, logRootPath, Level.FATAL, fatalAppenderName);
                appenderRefMap.put(Level.FATAL, AppenderRef.createAppenderRef(fatalAppenderName, null, null));
                appenderMap.put(Level.FATAL, fatalAppender);
            }
        }
        // Root -> Logger配置
        List<AppenderRef> appenderRefList = new ArrayList<>(appenderRefMap.values());
        appenderRefList.add(consoleAppenderRef);
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.ALL, LogManager.ROOT_LOGGER_NAME, "true", appenderRefList.toArray(new AppenderRef[0]), null, config, null);
        loggerConfig.addAppender(consoleAppender, null, null);
        for (Appender appender : appenderMap.values()) {
            loggerConfig.addAppender(appender, null, null);
        }
        // 针对特定包的Logger配置,"" 就是所有,也可以指定路径输出，可多个addLogger
        // 默认 必须
        config.addLogger(LogManager.ROOT_LOGGER_NAME, loggerConfig);
        // 默认 选配
        if (packLoggerList.size() > 0) {
            for (PackLoggerVo packLoggerVo : packLoggerList) {
                List<AppenderRef> appenderRefListTemp = new ArrayList<>();
                List<Appender> appenderListTemp = new ArrayList<>();
                if (packLoggerVo.isConsoleFlag()) {
                    appenderRefListTemp.add(consoleAppenderRef);
                    appenderListTemp.add(consoleAppender);
                }
                if (packLoggerVo.isFileFlag()) {
                    Level[] packLoggerVoLevels = packLoggerVo.getLevels();
                    for (Level loggerVoLevel : packLoggerVoLevels) {
                        if (appenderRefMap.containsKey(loggerVoLevel)) {
                            appenderRefListTemp.add(appenderRefMap.get(loggerVoLevel));
                            appenderListTemp.add(appenderMap.get(loggerVoLevel));
                        }
                    }
                }
                if (appenderRefListTemp.size() > 0) {
                    LoggerConfig loggerConfig2 = LoggerConfig.createLogger(false, Level.ALL, packLoggerVo.getPackageName(), "true", appenderRefListTemp.toArray(new AppenderRef[0]), null, config, null);
                    for (Appender appender : appenderListTemp) {
                        loggerConfig2.addAppender(appender, null, null);
                    }
                    config.addLogger(packLoggerVo.getPackageName(), loggerConfig2);
                }
            }
        }
        ctx.updateLoggers();
    }

    public static class Builder {

        private boolean traceFlag = false;
        private boolean debugFlag = false;
        private boolean infoFlag = false;
        private boolean warnFlag = false;
        private boolean errorFlag = false;
        private boolean fatalFlag = false;
        private List<PackLoggerVo> packLoggerList = new ArrayList<>();

        public Builder withTraceFlag(final boolean traceFlag) {
            this.traceFlag = traceFlag;
            return this;
        }

        public Builder withDebugFlag(boolean debugFlag) {
            this.debugFlag = debugFlag;
            return this;
        }

        public Builder withInfoFlag(boolean infoFlag) {
            this.infoFlag = infoFlag;
            return this;
        }

        public Builder withWarnFlag(boolean warnFlag) {
            this.warnFlag = warnFlag;
            return this;
        }

        public Builder withErrorFlag(boolean errorFlag) {
            this.errorFlag = errorFlag;
            return this;
        }

        public Builder withFatalFlag(boolean fatalFlag) {
            this.fatalFlag = fatalFlag;
            return this;
        }

        public Builder addPackLogger(final String packageName, boolean consoleFlag, boolean fileFlag, final Level... levels) {
            if (packageName != null && packageName.length() > 0) {
                packLoggerList.add(new PackLoggerVo().setPackageName(packageName).setConsoleFlag(consoleFlag).setFileFlag(fileFlag).setLevels(levels));
            }
            return this;
        }

        public Log4j2Util build() {
            return new Log4j2Util(traceFlag, debugFlag, infoFlag, warnFlag, errorFlag, fatalFlag, packLoggerList);
        }
    }

    public static class PackLoggerVo {
        private String packageName;
        private boolean consoleFlag;
        private boolean fileFlag;
        private Level[] levels;

        String getPackageName() {
            return packageName;
        }

        PackLoggerVo setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        boolean isConsoleFlag() {
            return consoleFlag;
        }

        PackLoggerVo setConsoleFlag(boolean consoleFlag) {
            this.consoleFlag = consoleFlag;
            return this;
        }

        boolean isFileFlag() {
            return fileFlag;
        }

        PackLoggerVo setFileFlag(boolean fileFlag) {
            this.fileFlag = fileFlag;
            return this;
        }

        Level[] getLevels() {
            return levels;
        }

        PackLoggerVo setLevels(Level[] levels) {
            this.levels = levels;
            return this;
        }
    }

}