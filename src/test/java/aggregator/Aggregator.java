package aggregator;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class Aggregator {

    private static final String JAR_NAME = "multiple-to-single-source-builder.jar";

    @Test
    public void shouldGenerateCodeInTarget() throws IOException, InterruptedException {
        // Given
        String[] command = {"java", "-jar", JAR_NAME};
        ProcessBuilder builder = new ProcessBuilder(command).directory(new File("./src/main/resources"));
        // When
        Process p = builder.start();
        p.waitFor();

        // Then
        Assert.assertEquals(0, p.exitValue());
        System.out.println("Processed file Player.java has been copied to clipboard");
    }
}
