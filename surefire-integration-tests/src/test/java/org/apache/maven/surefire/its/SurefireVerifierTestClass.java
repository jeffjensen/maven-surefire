package org.apache.maven.surefire.its;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import junit.framework.TestCase;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains commonly used featurtes for most tests, encapsulating
 * common use cases
 *
 * @author Kristian Rosenvold
 */
public abstract class SurefireVerifierTestClass
    extends AbstractSurefireIntegrationTestClass
{
    private final String testProject;
    private final File testDir;
    private final List cliOptions = new ArrayList(  );
    private final List goals;
    private final Verifier verifier;


    protected SurefireVerifierTestClass( String testProject )
    {
        try
        {
        this.testProject = testProject;
        testDir = ResourceExtractor.simpleExtractResources( getClass(), "/fork-consoleOutput" );
        this.goals = getInitialGoals();
            this.verifier = new Verifier( testDir.getAbsolutePath() );
        }
        catch ( VerificationException e )
        {
            throw new RuntimeException(  e );
        }
        catch ( IOException e )
        {
            throw new RuntimeException(  e );
        }
    }

    protected SurefireVerifierTestClass failNever(){
        cliOptions.add( "-fn" );
        return this;
    }
    protected SurefireVerifierTestClass addGoal(String goal){
        goals.add( goal );
        return this;
    }

    protected Verifier execute(String goal)
        throws VerificationException
    {
        addGoal( goal);
        verifier.setCliOptions(  cliOptions );
        verifier.executeGoals( goals );
        verifier.resetStreams();
        return verifier;
    }

    protected String getSurefireReportsFile(String fileName){
        File targetDir = new File( testDir, "target/surefire-reports" );
        File target= new File( targetDir, fileName);
        return target.getAbsolutePath();
    }
}
