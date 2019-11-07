package com.willy.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Hello world!
 *
 */
@Mojo( name = "sayhi",defaultPhase = LifecyclePhase.COMPILE)
public class App  extends AbstractMojo
{
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		System.out.println("Hello Execute"+this.name);
	}
	
	@Parameter( property = "sayhi.name", defaultValue = "Willy" )
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
