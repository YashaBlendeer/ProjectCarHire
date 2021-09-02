package com.yashablendeer.carhire.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FormView entity, used for creating sorting requests
 *
 * @author yaroslava
 * @version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormView {
    String message;
    String field;
}
