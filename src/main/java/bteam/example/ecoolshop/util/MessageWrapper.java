package bteam.example.ecoolshop.util;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageWrapper {
    private String to;
    private String header;
    private String text;
}
