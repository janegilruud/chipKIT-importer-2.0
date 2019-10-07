/*
 * Copyright (c) 2017 Microchip Technology Inc. and its subsidiaries (Microchip). All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.microchip.mplab.nbide.embedded.arduino.importer;

import com.microchip.mplab.nbide.embedded.arduino.utils.CopyingFileVisitor;
import com.microchip.mplab.nbide.embedded.makeproject.api.configurations.MakeConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class LibCoreBuilder extends AbstractMakeAssistant {

    
    public static final String LIB_CORE_NAME = "Core";
    public static final String LIB_CORE_FILENAME = "lib" + LIB_CORE_NAME + ".a";
    
    private final Path sourceDir;
    private Path buildDirPath;
    private BoardConfiguration boardConfiguration;
    private GCCToolFinder toolFinder;
    private Path libCorePath;
    private Path packPath;
    private MakeConfiguration makeConfiguration;
    private String archiveCommand;
    

    public LibCoreBuilder() {
        this.sourceDir = null;
    }
    
    public LibCoreBuilder( Path sourceDir ) {
        this.sourceDir = sourceDir;
    }

    @Override
    public String getMakefileName() {
        return "Makefile-" + LIB_CORE_NAME;
    }

    public String getArchiveCommand() {
        return archiveCommand;
    }

    public Path getLibCorePath() {
        return libCorePath;
    }

    @Override
    public Path getBuildDirPath() {
        return buildDirPath;
    }

    @Override
    public BoardConfiguration getBoardConfiguration() {
        return boardConfiguration;
    }
    
    @Override
    public String getTargetName() {
        return LIB_CORE_FILENAME;
    }

    @Override
    public GCCToolFinder getToolFinder() {
        return toolFinder;
    }

    public void build( Path makefilePath, GCCToolFinder toolFinder ) throws IOException, InterruptedException {
        build(makefilePath, toolFinder, null);
    }
    
    // TODO: Cleanup temp directories even after failed import
    public void build( Path makefilePath, GCCToolFinder toolFinder, Consumer<String> messageConsumer ) throws IOException, InterruptedException {
        this.buildDirPath = Files.createTempDirectory("build");
        this.toolFinder = toolFinder;
        this.libCorePath = buildDirPath.resolve(LIB_CORE_FILENAME);
        updateMakefile( makefilePath, toolFinder );
        invokeMakeTool(messageConsumer, messageConsumer);
    }
    
    public void build( BoardConfiguration boardConfiguration, GCCToolFinder toolFinder, Path packPath, MakeConfiguration makeConfiguration, Consumer<String> messageConsumer ) throws IOException, InterruptedException {
        this.buildDirPath = Files.createTempDirectory("build");
        this.boardConfiguration = boardConfiguration;
        this.toolFinder = toolFinder;
        this.libCorePath = buildDirPath.resolve(LIB_CORE_FILENAME);
        this.packPath = packPath;
        this.makeConfiguration = makeConfiguration;
        if ( sourceDir != null ) {
            copySourceFiles();
        }
        build( messageConsumer, messageConsumer );
    }
    
    @Override
    protected void generateMakefile() throws IOException {
        super.generateMakefile();
        
        // Generate archiver command:
        Map <String,String> runtimeData = new HashMap<>();
        runtimeData.put( getToolsPathKey(), getToolchainPath().toString() );
        runtimeData.put( "archive_file_path", LIB_CORE_FILENAME );
        getObjectFilenames().forEach( n -> {
            runtimeData.put("object_file", n);
            getMakefileContents().add( "\t" + boardConfiguration.getValue("recipe.ar.pattern", runtimeData).get() );
        });
    } 

    @Override
    protected String mapSourceFilePath(Path sourceFilePath) {
        if ( sourceDir != null ) {
            return sourceFilePath.getFileName().toString();
        } else {
            return super.mapSourceFilePath(sourceFilePath);
        }
    }
    
    @Override
    protected String buildIncludesSection(BoardConfiguration boardConfiguration) {
        StringBuilder ret = new StringBuilder();
        if (packPath != null) {
            /* Because the MPLAB X 5.25 API does not expose proper functions to
            extract necessary pack information it is necessary to search the
            packs for the specs-files and cheat with some hard-coding. */
            ret.append(" \"-I").append(packPath.toString()).append("/include\"");
            String devName = makeConfiguration.getDevice().getName().toLowerCase();
            try (Stream<Path> walk = Files.walk(packPath.resolve("gcc"))) {
                List<String> result = walk.filter(Files::isDirectory)
                        .map(x -> x.toString())
                        .filter(f -> f.endsWith(devName))
                        .collect(Collectors.toList());
                result.forEach(System.out::println);
                if(!result.isEmpty()) {
                    ret.append(" \"-B").append(result.get(0)).append("\"");
                }
            } catch (IOException e) {
                // Ignore exception
            }
        }
        if (sourceDir != null) {
            ret.append(" \"-I.").append("\"");
        } else {
            ret.append(super.buildIncludesSection(boardConfiguration));
        }
        return ret.toString();
    }


    //*************************************************
    //*************** PRIVATE METHODS *****************
    //*************************************************
    private void copySourceFiles() throws IOException {
        Files.walkFileTree(sourceDir, new CopyingFileVisitor(sourceDir, buildDirPath));
    }
    
    private static void updateMakefile(Path makefilePath, GCCToolFinder toolFinder) throws IOException {
        throw new UnsupportedOperationException("Updating a Makefile is not supported yet!");
//        Path compilerPath = toolFinder.findTool( LanguageTool.CCCompiler );
//        List<String> makefileLines = Files.readAllLines(makefilePath);
//        for ( int i=0; i<makefileLines.size(); i++ ) {
//            String line = makefileLines.get(i).trim();
//            if ( line.startsWith( TOOLS_DIR ) ) {
//                makefileLines.set( i, TOOLS_DIR + "=" + compilerPath.getParent().toString() );
//                break;
//            }
//        }
//        Files.write(makefilePath, makefileLines);
    }

}