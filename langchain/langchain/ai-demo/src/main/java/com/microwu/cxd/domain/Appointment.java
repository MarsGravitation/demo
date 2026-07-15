package com.microwu.cxd.domain;

import lombok.Data;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   17:27
 * Update History:
 * Author        Time            Content
 */
@Data
public class Appointment {

    private Long id;
    private String username;
    private String idCard;
    private String department;
    private String date;
    private String time;
    private String doctorName;

}
