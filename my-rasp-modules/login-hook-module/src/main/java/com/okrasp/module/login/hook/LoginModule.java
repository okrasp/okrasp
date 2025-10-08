package com.okrasp.module.login.hook;

import com.okrasp.api.AbstractModule;
import com.okrasp.api.ProcessController;
import com.okrasp.api.annotation.ModuleInfo;
import com.okrasp.api.listener.RaspHookListener;
import com.okrasp.api.matcher.ClassMatcher;
import com.okrasp.api.matcher.ClassWatchBuilder;

@ModuleInfo(desc = "login module")
public class LoginModule extends AbstractModule {

    @Override
    public void init() {
        new ClassWatchBuilder(moduleEventWatcher)
                .onClass(new ClassMatcher("com/example/springdemoexample/Controller")
                        .onMethod("login", new MyLoginHookListener()))
                .build();
    }

    class MyLoginHookListener extends RaspHookListener {
        @Override
        public void atEnter(Object[] args, Object thiss) throws Throwable {
            if (args != null && args.length == 2) {
                String username = (String) args[0];
                String password = (String) args[1];
                logger.info("username: " + username + ", password: " + password);
            }
        }
    }
}
