package cn.mw.monitor.customPage.dto;

import cn.mw.monitor.service.model.param.MwPagefieldByModelTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MwPageCustomColByModelDTO extends MwPagefieldByModelTable {

    // id
    private Integer customId;
    // 列ID
    private Integer colId;
    // 用户ID
    private Integer userId;
    // 是否排序
    private Boolean sortable;
    // 宽度
    private Integer width;
    // 是否可见
    private Boolean visible;
    // 顺序数
    private Integer orderNumber;
    //是否还原
    private Integer deleteFlag;

}
