package org.dbshell.jcommander.parameters;

import com.beust.jcommander.SubParameter;

public class AddJndi {
    @SubParameter(order = 0)
    public String jndiName;

    @SubParameter(order = 1)
    public String contextName;
}