package com.runu.web_server.config.flex;

import com.mybatisflex.core.audit.*;

public class MyMessageFactory implements MessageFactory {
    @Override
    public AuditMessage create() {
        AuditMessage auditMessage = new AuditMessage();
        MessageCollector collector = new ConsoleMessageCollector();
        AuditManager.setMessageCollector(collector);
        AuditManager.setAuditEnable(true);
        return auditMessage;
    }
}
