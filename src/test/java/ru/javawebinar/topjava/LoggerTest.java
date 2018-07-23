package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest implements TestRule {

    private Logger log;

    public Logger getLogger() {
        return log;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                log = LoggerFactory.getLogger(description.getTestClass().getName() + '.' + description.getDisplayName());
                base.evaluate();
            }
        };
    }
}
