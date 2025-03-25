<template>
  <div class="main">
    <el-card class="w-[100%] full">
      <div class="flex flex-row justify-start w-[75%] float-left">
        <IconifyIconOffline
          :icon="bustsInSilhouette"
          width="25px"
          height="25px"
        />
        <span class="pl-2 head-title pr-10">角色管理</span>
        <div class="w-[280px]">
          <span class="pl-2 pr-2">角色名称 </span>
          <el-input
            v-model="queryData.roleName"
            class="!w-[60%]"
            clearable
            placeholder="输入角色名称模糊查询"
            @input="getRoleList()"
          />
        </div>
        <div class="pl-2 pr-2">
          <el-button type="primary" :icon="Search" @click="getRoleList()"
            >搜索</el-button
          >
        </div>
      </div>

      <div class="pl-2 pr-2 float-right w-[20%] flex flex-row justify-end">
        <el-button
          v-perms="'role:add'"
          type="success"
          :icon="CirclePlus"
          @click="add()"
          >添加角色</el-button
        >
      </div>
      <!-- <el-divider></el-divider> -->
      <div class="pt-4">
        <el-table :data="roleList" :height="roleTableHeader">
          <el-table-column label="编号" width="200">
            <template #default="{ row }">
              <span>{{ row.roleId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="角色名">
            <template #default="{ row }">
              <span>{{ row.roleName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="编码">
            <template #default="{ row }">
              <span>{{ row.roleCode }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注">
            <template #default="{ row }">
              <span>{{ row.note }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-perms-or="['role:set', 'role:del']"
            label="操作"
            width="400"
          >
            <template #default="{ row }">
              <el-button
                v-perms="'role:set'"
                :icon="Key"
                type="primary"
                @click="setRoleAuthority(row)"
                >分配权限</el-button
              >
              <el-button
                v-perms="'role:set'"
                :icon="EditPen"
                type="warning"
                @click="setRole(row)"
                >编辑</el-button
              >
              <el-button
                v-perms="'role:del'"
                :icon="Delete"
                type="danger"
                @click="del(row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pt-4">
        <el-pagination
          v-model:current-page="pageData.pageNumber"
          v-model:page-size="pageData.pageSize"
          :pager-count="5"
          background
          :page-sizes="[10, 50, 100, 200]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageData.total"
          @size-change="getRoleList"
          @current-change="getRoleList"
        />
      </div>
    </el-card>
    <el-dialog
      v-model="RoleDialog.show"
      v-loading="RoleDialog.loading"
      :title="(RoleDialog.usage.AorU ? '修改' : '添加') + '角色'"
      width="20%"
    >
      <el-form
        ref="roleFormRef"
        :model="RoleDialog.data"
        :rules="RoleDialog.rules"
        label-position="left"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input
            v-model="RoleDialog.data.roleName"
            placeholder="请输入角色名称"
          />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input
            v-model="RoleDialog.data.roleCode"
            placeholder="请输入角色编码"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="RoleDialog.data.note"
            :rows="4"
            type="textarea"
            placeholder="填写备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="RoleDialog.show = false">取消</el-button>
          <el-button type="primary" @click="toRole(roleFormRef)">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="AuthorityDiaLog.show" title="分配权限" width="20%">
      <el-tree
        v-if="authorTreeShow"
        ref="authorityTreeRef"
        show-checkbox
        default-expand-all
        node-key="id"
        highlight-current
        check-strictly
        :data="authorityElTree"
        :default-checked-keys="AuthorityDiaLog.data"
        @check="authorityTreeChange"
      >
        <template #default="{ data }">
          <span>{{ data.label }}</span>
        </template>
      </el-tree>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="AuthorityDiaLog.show = false">取消</el-button>
          <el-button type="primary" @click="toAuthority()"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  delRole,
  getPageByQuery,
  getRoleById,
  postRole
} from "@/api/role/index";
import { postRoleAuthority } from "@/api/role/rolePower";
import type { RoleType } from "@/api/role/index";
import { type Power, getElTree } from "@/api/power/index";
import { onMounted, reactive, ref } from "vue";
import { ElMessageBox, FormInstance } from "element-plus";
import { message } from "@/utils/message";
import { ElTree } from "element-plus";
import {
  Delete,
  Connection,
  EditPen,
  CirclePlus,
  Refresh,
  Search,
  Key
} from "@element-plus/icons-vue";
import bustsInSilhouette from "@iconify-icons/twemoji/busts-in-silhouette";

const roleList = ref<Array<RoleType>>([]);
const pageData = reactive({
  pageNumber: 1,
  pageSize: 10,
  total: 0
});
const queryData = reactive({
  roleName: null
});
const roleTableHeader = ref();
const RoleDialog = reactive<iElDiaLog>({
  show: false,
  loading: false,
  data: null,
  usage: {
    AorU: false
  },
  rules: {
    roleName: [
      {
        required: true,
        message: "请填写角色名称",
        trigger: "blur"
      }
    ],
    roleCode: [
      {
        required: true,
        message: "请填写角色编码",
        trigger: "blur"
      }
    ]
  }
});
const roleFormRef = ref();
const authorityElTree = ref<Array<iElTree>>();
const AuthorityDiaLog = reactive<iElDiaLog>({
  show: false,
  data: null,
  usage: {
    id: null
  }
});
const authorTreeShow = ref<boolean>(false);
const authorityTreeRef = ref<InstanceType<typeof ElTree>>();

onMounted(() => {
  getRoleList();
  getCardDetailsTableHeight();
});

// 动态计算表格高度
const getCardDetailsTableHeight = () => {
  const tableH = 270; //距离页面下方的高度
  const tableHeight = window.innerHeight - tableH;
  if (tableHeight <= 100) {
    roleTableHeader.value = 100;
  } else {
    roleTableHeader.value = window.innerHeight - tableH;
  }
};
// 监听窗口变化动态设置表格高度
window.onresize = () => {
  getCardDetailsTableHeight();
};

const add = () => {
  RoleDialog.loading = true;
  RoleDialog.data = {
    roleId: null,
    roleName: null,
    roleCode: null,
    note: null
  };
  RoleDialog.usage.AorU = false;
  RoleDialog.show = true;
  RoleDialog.loading = false;
};

const setRole = (row: RoleType) => {
  RoleDialog.loading = true;
  getRoleById(row.roleId)
    .then((res: R<RoleType>) => {
      if (res.code == 200) {
        RoleDialog.data = res.data;
        RoleDialog.usage.AorU = true;
        RoleDialog.show = true;
      } else {
        message("获取用户信息失败" + res.msg, { type: "error" });
      }
    })
    .catch(error => {
      console.error("获取用户信息异常", error);
    })
    .finally(() => {
      RoleDialog.loading = false;
    });
};

const setRoleAuthority = (row: RoleType) => {
  authorTreeShow.value = false;
  getElTree()
    .then((res: R<iElTree>) => {
      if (res.code == 200) {
        authorityElTree.value = res.data;
        getRoleById(row.roleId)
          .then((byRoleRes: R<Array<Power>>) => {
            if (byRoleRes.code == 200) {
              AuthorityDiaLog.usage.id = row.roleId;
              AuthorityDiaLog.data = byRoleRes.data.powers.map((tree: any) => {
                return tree.powerId;
              });
              console.log(AuthorityDiaLog.data);
              AuthorityDiaLog.show = true;
              authorTreeShow.value = true;
            } else {
              message("获取角色的权限失败" + byRoleRes.msg, { type: "error" });
            }
          })
          .catch(error => {
            console.log("获取角色的权限异常", error);
          });
      } else {
        message("获取权限列表失败" + res.msg, { type: "error" });
      }
    })
    .catch(error => {
      console.log("获取权限列表异常", error);
    });
};

/**
 * 权限树选中值编号执行这个函数
 */
const childrenValues = ref<Array<{ id: number; label: string }>>([]);
const authorityTreeChange = () => {
  const checkedNodes = (authorityTreeRef.value as any).getCheckedNodes();
  childrenValues.value = checkedNodes.flatMap((row: iElTree) => {
    return [{ id: row.id, label: row.label }];
  });
};

/**
 * 赋予权限
 */
const toAuthority = () => {
  authorityTreeChange();
  const data: number[] = childrenValues.value.map(({ id }) => {
    return id;
  });
  postRoleAuthority(AuthorityDiaLog.usage.id, data + "")
    .then((res: R<any>) => {
      if (res.code == 200) {
        message("赋予权限成功", { type: "success" });
        AuthorityDiaLog.show = false;
      } else {
        message("赋予权限失败" + res.msg, { type: "error" });
      }
    })
    .catch(error => {
      console.error("赋予权限异常", error);
    });
};

const toRole = async (elForm: FormInstance | undefined) => {
  if (!elForm) return;
  await elForm.validate((valid, fields) => {
    if (valid) {
      RoleDialog.loading = true;
      if (!RoleDialog.usage.AorU) {
        postRole(RoleDialog.data)
          .then((res: R<any>) => {
            if (res.code == 200) {
              getRoleList();
              RoleDialog.show = false;
              RoleDialog.loading = false;
              message("添加角色成功", { type: "success" });
            } else {
              message("添加角色失败" + res.msg, { type: "error" });
            }
          })
          .catch(error => {
            message("添加角色过程中遇到异常", error);
          })
          .finally(() => {
            RoleDialog.loading = false;
          });
      } else {
        postRole(RoleDialog.data).then((res: R<any>) => {
          if (res.code == 200) {
            getRoleList();
            RoleDialog.show = false;
            RoleDialog.loading = false;
            message("编辑角色信息成功", { type: "success" });
          } else {
            message("编辑角色信息失败" + res.msg, { type: "error" });
          }
        });
      }
    } else {
      console.log("无法通过验证", fields);
    }
  });
};

const del = (row: RoleType) => {
  ElMessageBox.confirm(
    "确认要删除此角色吗?可能会影响到系统的正常使用!",
    "提示",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning"
    }
  )
    .then(() => {
      delRole(row.roleId)
        .then((res: R<any>) => {
          if (res.code == 200) {
            message("删除角色成功", { type: "success" });
            getRoleList();
          } else {
            message("删除角色失败", { type: "success" });
          }
        })
        .catch(error => {
          console.error("删除角色异常", error);
        });
    })
    .catch(() => {
      message("取消删除角色");
    });
};

/** !http **/

/**
 *! 获取用户列表
 */
const getRoleList = () => {
  getPageByQuery({
    pageNumber: pageData.pageNumber,
    pageSize: pageData.pageSize,
    roleName: queryData.roleName
  })
    .then((res: R<any>) => {
      if (res.code == 200) {
        roleList.value = res.data.records;
        pageData.total = res.data.totalRow;
      } else {
        message("获取用户列表失败", { type: "error" });
      }
    })
    .catch(error => {
      console.error("获取用户列表异常", error);
    });
};
</script>

<style lang="scss" scoped></style>
