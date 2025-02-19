/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.pmml.models.tree.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kie.pmml.commons.model.tuples.KiePMMLProbabilityConfidence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.kie.pmml.models.tree.model.KiePMMLTreeTestUtils.getRandomKiePMMLScoreDistributions;

public class KiePMMLNodeTest {

    @Test
    public void getProbabilityConfidenceMap() {
        LinkedHashMap<String, KiePMMLProbabilityConfidence> retrieved = KiePMMLNode.getProbabilityConfidenceMap(null, 1.0);
        assertNotNull(retrieved);
        assertTrue(retrieved.isEmpty());
        retrieved = KiePMMLNode.getProbabilityConfidenceMap(Collections.emptyList(), 1.0);
        assertNotNull(retrieved);
        assertTrue(retrieved.isEmpty());
        List<KiePMMLScoreDistribution> kiePMMLScoreDistributions = getRandomKiePMMLScoreDistributions(false);
        retrieved = KiePMMLNode.getProbabilityConfidenceMap(kiePMMLScoreDistributions, 1.0);
        assertNotNull(retrieved);
        assertEquals(kiePMMLScoreDistributions.size(), retrieved.size());
    }

    @Test
    public void evaluateProbabilityConfidenceMap() {
        List<KiePMMLScoreDistribution> kiePMMLScoreDistributions = getRandomKiePMMLScoreDistributions(false);
        int totalRecordCount = kiePMMLScoreDistributions.stream()
                .map(KiePMMLScoreDistribution::getRecordCount)
                .reduce(0, Integer::sum);
        final double missingValuePenalty = (double) new Random().nextInt(100)/10;
        LinkedHashMap<String, KiePMMLProbabilityConfidence> retrievedNoProbability = KiePMMLNode.getProbabilityConfidenceMap(kiePMMLScoreDistributions, missingValuePenalty);
        assertNotNull(retrievedNoProbability);
        kiePMMLScoreDistributions.forEach(kiePMMLScoreDistribution -> {
            assertTrue(retrievedNoProbability.containsKey(kiePMMLScoreDistribution.getValue()));
            KiePMMLProbabilityConfidence kiePMMLProbabilityConfidence = retrievedNoProbability.get(kiePMMLScoreDistribution.getValue());
            assertNotNull(kiePMMLProbabilityConfidence);
            double probabilityExpected = (double) kiePMMLScoreDistribution.getRecordCount() / (double) totalRecordCount;
            double confidenceExpected = kiePMMLScoreDistribution.getConfidence() * missingValuePenalty;
            assertEquals(probabilityExpected, kiePMMLProbabilityConfidence.getProbability(), 0.000000001);
            assertEquals(confidenceExpected, kiePMMLProbabilityConfidence.getConfidence(), 0.000000001);
        });
        //
        kiePMMLScoreDistributions = getRandomKiePMMLScoreDistributions(true);
        LinkedHashMap<String, KiePMMLProbabilityConfidence> retrievedProbability = KiePMMLNode.getProbabilityConfidenceMap(kiePMMLScoreDistributions, missingValuePenalty);
        assertNotNull(retrievedNoProbability);
        kiePMMLScoreDistributions.forEach(kiePMMLScoreDistribution -> {
            assertTrue(retrievedProbability.containsKey(kiePMMLScoreDistribution.getValue()));
            KiePMMLProbabilityConfidence kiePMMLProbabilityConfidence = retrievedProbability.get(kiePMMLScoreDistribution.getValue());
            assertNotNull(kiePMMLProbabilityConfidence);
            double probabilityExpected = kiePMMLScoreDistribution.getProbability();
            double confidenceExpected = kiePMMLScoreDistribution.getConfidence() * missingValuePenalty;
            assertEquals(probabilityExpected, kiePMMLProbabilityConfidence.getProbability(), 0.000000001);
            assertEquals(confidenceExpected, kiePMMLProbabilityConfidence.getConfidence(), 0.000000001);
        });
    }
}