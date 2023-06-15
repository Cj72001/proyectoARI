package com.uca.spring.model;

public class ParametrosDTO {
	private String content;
    private String key;
    private String delimit;
    
    public ParametrosDTO(String content, String key, String delimit) {
        this.content = content;
        this.key = key;
        this.delimit = delimit;
    }
    
 // Métodos getter y setter para acceder a los campos

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDelimit() {
        return delimit;
    }

    public void setDelimit(String delimit) {
        this.delimit = delimit;
    }

}
