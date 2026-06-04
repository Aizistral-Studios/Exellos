package com.aizistral.exellos;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.ContextAwareBase;

public class LogbackConfigurator extends ContextAwareBase implements Configurator {

	public LogbackConfigurator() {
		// NO-OP
	}

	@Override
	public ExecutionStatus configure(LoggerContext context) {
		this.addInfo("Setting up custom configuration.");

		LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
		encoder.setContext(context);

		PatternLayout layout = new PatternLayout();
		layout.setPattern("[%d{HH:mm:ss.SSS}] [T:%thread] [%level] [%logger{16}]: %msg%n");
		layout.setContext(context);
		layout.start();

		encoder.setLayout(layout);

		ConsoleAppender<ILoggingEvent> console = new ConsoleAppender<>();
		console.setContext(context);
		console.setName("console");
		console.setEncoder(encoder);
		console.start();

		Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.addAppender(console);

		return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY;
	}

}

