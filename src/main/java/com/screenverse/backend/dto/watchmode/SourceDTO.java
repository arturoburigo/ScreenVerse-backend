package com.screenverse.backend.dto.watchmode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a streaming source from the WatchMode API
 * Contains information about where a title is available for streaming, including platform, region, and pricing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceDTO {
    private Integer source_id;
    private String name;
    private String type;
    private String region;
    private String web_url;
    private String format;
    private Double price;
    private String seasons;
    private String episodes;
}
