package edu.cmu.cs214.hw6;

import edu.cmu.cs214.hw6.framework.core.ContentClassifyFramework;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class FrameworkTest {
    private ContentClassifyFramework framework;
    private List<String> mockInput;
    private String mockContent1;
    private String mockContent2;

    @Before
    public void setUp() {
        mockContent1 = "The articles in English are the definite article the and the indefinite articles a and an. The definite article " +
                "is used when the speaker believes that the listener knows the identity of the noun's referent " +
                "(because it is obvious, because it is common knowledge, or because it was mentioned in the same sentence or an earlier sentence). " +
                "The indefinite article is used when the speaker believes that the listener does not have to be told the identity of the referent. ";
        mockContent2 = "123";
        mockInput = new ArrayList<>();
        mockInput.add(mockContent1 + "#~#456");
        mockInput.add(mockContent2 + "#~#789");
        framework = new ContentClassifyFramework(mockInput);
        framework.processFromInput();
    }

    @Test
    public void testProcessInputSize() {
        List<String> res = framework.getTextContent();
        System.out.println(res);
        assertEquals(1, res.size());
    }

    @Test
    public void testProcessInputContent() {
        List<String> res = framework.getTextContent();
        assertEquals(mockContent1, res.get(0));
    }

    @Test
    public void testProcessInputOther() {
        List<String> res = framework.getOtherInfo();
        assertEquals("456", res.get(0));
    }
}
