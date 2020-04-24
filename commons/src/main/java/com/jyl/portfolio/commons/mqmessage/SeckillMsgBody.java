package com.jyl.portfolio.commons.mqmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMsgBody implements Serializable {
    private Calendar msgId;
    private Long seckillSwagId;
    private Long userPhone;
}
