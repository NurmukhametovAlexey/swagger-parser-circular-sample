package com.example;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SwaggerTest {

    static OpenAPI openAPI;

    static {
        ParseOptions options = new ParseOptions();
        options.setResolve(true);
        options.setResolveFully(true);
        options.setResolveCombinators(true);

        SwaggerParseResult swaggerParseResult = new OpenAPIV3Parser().readLocation(
                "circular.yaml",
                new ArrayList<>(),
                options
        );
        openAPI = swaggerParseResult.getOpenAPI();
    }


    @Test
    public void testCircularObjectChainedDependency() {
        Schema treeNode = openAPI.getComponents().getSchemas().get("TreeNode");
        // here everything is fine
        Assertions.assertNotNull(treeNode.getProperties());

        Schema treeNodeValue = (Schema) treeNode.getProperties().get("value");
        Schema ofNode = (Schema) treeNodeValue.getProperties().get("ofNode");

        // properties should be like in treeNode above
        Assertions.assertNotNull(ofNode.getProperties());
    }

    @Test
    public void testCircularObjectSelfDependency() {
        Schema treeNode = openAPI.getComponents().getSchemas().get("TreeNode");
        // here everything is fine
        Assertions.assertNotNull(treeNode.getProperties());

        Schema parentNode = (Schema) treeNode.getProperties().get("parentNode");

        // properties should be like in treeNode above
        Assertions.assertNotNull(parentNode.getProperties());
    }

    @Test
    public void testCircularArraySelfDependency() {
        Schema treeNode = openAPI.getComponents().getSchemas().get("TreeNode");
        // here everything is fine
        Assertions.assertNotNull(treeNode.getProperties());

        Schema childNodes = (Schema) treeNode.getProperties().get("childNodes");

        // properties should be like in treeNode above
        Assertions.assertNotNull(childNodes.getItems().getProperties());
    }
}
