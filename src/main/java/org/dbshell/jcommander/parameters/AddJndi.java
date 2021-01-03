package org.dbshell.jcommander.parameters;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.SubParameter;

import java.util.Map;

public class AddJndi {
    @SubParameter(order = 0)
    public String jndiName;

    @SubParameter(order = 1)
    public String contextName;
}