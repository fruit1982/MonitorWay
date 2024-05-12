package cn.mw.monitor.model.param.virtual;

import cn.mw.monitor.service.assets.model.GroupDTO;
import cn.mw.monitor.service.user.dto.OrgDTO;
import cn.mw.monitor.service.assets.model.UserDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author qzg
 * @date 2022/9/27
 */
@Data
public class ModelVirtualUserParam {
    private String typeId;

    @ApiModelProperty("用户组ID列表")
    private List<Integer> groupIds;

    @ApiModelProperty("负责人ID列表")
    private List<Integer> userIds;

    @ApiModelProperty("机构ID列表")
    private List<List<Integer>> orgIds;

    private List<UserDTO> principal;

    private List<OrgDTO> department;

    private List<GroupDTO> groups;
}
