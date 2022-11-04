package com.capgemini.bedwards.almon.almonmonitoringcore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class FormUtil {

    @SneakyThrows
    public static ObjectNode getObjectNodeFromRequest(Map<String, String[]> parameterMap) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonRes = mapper.createObjectNode();

        parameterMap.forEach((s, strings) -> {
            String[] parts = s.split("\\.");
            ObjectNode node = jsonRes;
            for (int index = 0; index < parts.length; index++) {
                String name = parts[index];
                log.info(name + " - " + index);
                if (index < parts.length - 1) {
                    if (!node.has(name))
                        node.set(name, mapper.createObjectNode());
                    node = (ObjectNode) node.get(name);
                } else {
                    node.put(name, strings[0]);
                }
            }
        });
        if (log.isDebugEnabled())
            log.debug("Json: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonRes));
        return jsonRes;
    }
}
