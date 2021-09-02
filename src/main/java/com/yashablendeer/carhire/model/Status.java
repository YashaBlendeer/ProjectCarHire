package com.yashablendeer.carhire.model;

/**
 * Status entity to determine order's and car's status
 *
 * @author yaroslava
 * @version 1.0
 */

public enum Status {
    //Order status
    WAITING,
    REJECTED,
    ACCEPTED,
    FINISHED,

    //Car status
    REPAIR,
    READY,

    //Paying status
    PAYED,
    UNPAYED

}
