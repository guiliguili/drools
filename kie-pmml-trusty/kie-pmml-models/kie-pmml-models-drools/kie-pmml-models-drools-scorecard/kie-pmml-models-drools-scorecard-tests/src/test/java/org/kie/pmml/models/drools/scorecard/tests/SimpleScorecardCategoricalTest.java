/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.pmml.models.drools.scorecard.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.api.runtime.PMMLRuntime;
import org.kie.pmml.models.tests.AbstractPMMLTest;

import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class SimpleScorecardCategoricalTest extends AbstractPMMLTest {

    private static final String FILE_NAME = "Simple-Scorecard_Categorical.pmml";
    private static final String MODEL_NAME = "SimpleScorecardCategorical";
    private static final String TARGET_FIELD = "Score";
    private static final String REASON_CODE1_FIELD = "Reason Code 1";
    private static final String REASON_CODE2_FIELD = "Reason Code 2";
    private static final String[] CATEGORY = new String[]{"classA", "classB", "classC", "classD", "classE", "NA"};
    private static PMMLRuntime pmmlRuntime;

    private String input1;
    private String input2;
    private double score;
    private String reasonCode1;
    private String reasonCode2;

    public SimpleScorecardCategoricalTest(String input1, String input2, double score, String reasonCode1,
                                          String reasonCode2) {
        this.input1 = input1;
        this.input2 = input2;
        this.score = score;
        this.reasonCode1 = reasonCode1;
        this.reasonCode2 = reasonCode2;
    }

    @BeforeClass
    public static void setupClass() {
        pmmlRuntime = getPMMLRuntime(FILE_NAME);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"classA", "classB", 25, "Input1ReasonCode", null},
                {"classA", "classA", -15, "Input1ReasonCode", "Input2ReasonCode"},
                {"classB", "classB", 87, null, null},
                {"classB", "classA", 47, "Input2ReasonCode", null},
        });
    }

    @Test
    public void testSimpleScorecardCategorical() {
        final Map<String, Object> inputData = new HashMap<>();
        inputData.put("input1", input1);
        inputData.put("input2", input2);
        PMML4Result pmml4Result = evaluate(pmmlRuntime, inputData, MODEL_NAME);

        Assertions.assertThat(pmml4Result.getResultVariables().get(TARGET_FIELD)).isNotNull();
        Assertions.assertThat(pmml4Result.getResultVariables().get(TARGET_FIELD)).isEqualTo(score);
        Assertions.assertThat(pmml4Result.getResultVariables().get(REASON_CODE1_FIELD)).isEqualTo(reasonCode1);
        Assertions.assertThat(pmml4Result.getResultVariables().get(REASON_CODE2_FIELD)).isEqualTo(reasonCode2);
    }

    @Test
    public void testSimpleScorecardCategoricalVerifyNoException() {
        getSamples().stream().map(sample -> evaluate(pmmlRuntime, sample, MODEL_NAME)).forEach(Assert::assertNotNull);
    }

    @Test
    public void testSimpleScorecardCategoricalVerifyNoReasonCodeWithoutScore() {
        getSamples().stream().map(sample -> evaluate(pmmlRuntime, sample, MODEL_NAME))
                .filter(pmml4Result -> pmml4Result.getResultVariables().get(TARGET_FIELD) == null)
                .forEach(pmml4Result -> {
                    assertFalse(pmml4Result.getResultVariables().containsKey(REASON_CODE1_FIELD));
                    assertFalse(pmml4Result.getResultVariables().containsKey(REASON_CODE2_FIELD));
                });
    }

    private List<Map<String, Object>> getSamples() {
        return IntStream.range(0, 10).boxed().map(i -> new HashMap<String, Object>() {{
            put("input1", CATEGORY[i % CATEGORY.length]);
            put("input2", CATEGORY[Math.abs(CATEGORY.length - i) % CATEGORY.length]);
        }}).collect(Collectors.toList());
    }
}
