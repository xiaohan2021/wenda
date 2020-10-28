package com.nowcoder.service;

import jdk.internal.util.xml.impl.Input;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/28
 */
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null){
                addWord(lineTxt.trim());
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }

    }

    // 增加敏感关键词
    public void addWord(String lineText){
        TrieNode tempNode = rootNode;

        for (int i = 0; i < lineText.length(); i++) {
            Character c = lineText.charAt(i);

            TrieNode node = tempNode.getSubNode(c);

            if(node == null){
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;

            if(i == lineText.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class TrieNode{
        // 是不是关键词的结尾
        private boolean end = false;

        // 当前节点下的所有子节点
        private Map<Character,TrieNode> subNodes = new HashMap<Character, TrieNode>();

        public void addSubNode(Character key, TrieNode node){
            subNodes.put(key, node);
        }

        public TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        void setKeywordEnd(boolean end){
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }

        StringBuilder result = new StringBuilder();

        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        if(position < text.length()){
            char c = text.charAt(position);

            tempNode = tempNode.getSubNode(c);

            if(tempNode == null){
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if(tempNode.isKeywordEnd()){
                // 发现敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] args){
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        s.addWord("嫖娼");
        System.out.println(s.filter("你好色情"));
    }
}
