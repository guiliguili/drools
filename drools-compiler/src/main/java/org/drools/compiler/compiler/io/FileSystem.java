/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.compiler.compiler.io;


import org.kie.memorycompiler.resources.KiePath;

public interface FileSystem {
    Folder getRootFolder();
    
    File getFile(KiePath path);
    default File getFile(String name) {
        return getFile(KiePath.of(name));
    }

    Folder getFolder(KiePath path);
    default Folder getFolder(String name) {
        return getFolder(KiePath.of(name));
    }
    
    boolean remove(File file);
    
    boolean remove(Folder folder);
}
