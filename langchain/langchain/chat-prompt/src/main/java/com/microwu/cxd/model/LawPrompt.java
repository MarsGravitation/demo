package com.microwu.cxd.model;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.Data;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   10:40
 * Update History:
 * Author        Time            Content
 */
@Data
@StructuredPrompt("根据中国{{legal}}法律，解答以下问题：{{question}}")
public class LawPrompt
{
    private String legal;
    private String question;
}
