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

package org.drools.core.rule;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import static org.kie.utll.xml.XStreamUtils.createNonTrustingXStream;

public class KieModuleMetaInfo implements Serializable {

    private static class Marshaller {
        private static final XStream xStream = createNonTrustingXStream( new DomDriver() );

        static {
            xStream.setClassLoader( KieModuleMetaInfo.class.getClassLoader() );
        }
    }

    private Map<String, TypeMetaInfo> typeMetaInfos;
    private Map<String, Set<String>> rulesByPackage;

    public KieModuleMetaInfo() { }

    public KieModuleMetaInfo(Map<String, TypeMetaInfo> typeMetaInfoMap, Map<String, Set<String>> rulesByPackage) {
        this.typeMetaInfos = typeMetaInfoMap;
        this.rulesByPackage = rulesByPackage;
    }

    public String marshallMetaInfos() {
        return Marshaller.xStream.toXML(this);
    }

    public static KieModuleMetaInfo unmarshallMetaInfos(String s) {
        return (KieModuleMetaInfo)Marshaller.xStream.fromXML(s);
    }

    public Map<String, TypeMetaInfo> getTypeMetaInfos() {
        return typeMetaInfos;
    }

    public Map<String, Set<String>> getRulesByPackage() {
        return rulesByPackage;
    }
}
