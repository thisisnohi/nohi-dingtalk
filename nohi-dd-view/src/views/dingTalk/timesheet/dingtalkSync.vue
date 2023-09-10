<template>
    <div class="layout-container">
        <div style="margin-top: 20px">
            <el-button :plain="true" :loading="syncLoading" @click="syncDept">刷新钉钉部门</el-button>
            <el-button :plain="true" :loading="syncLoading" @click="syncKqGroup">刷新钉钉考勤组</el-button>
            <el-button :plain="true" :loading="syncLoading" @click="syncUser">刷新钉钉用户</el-button>
        </div>
    </div>
</template>

<script>
import {syncDept, syncKqGroup, syncUser} from '@/api/dingtalk/kaoqing'

export default {
    provide() {
        return {
            closeDialog: this.closeDialog,
            queryBtn: this.queryBtn
        }
    },
    data() {
        return {
            syncLoading: false
        }
    },
    methods: {
        // 刷新部门
        async syncDept() {
            const $this = this
            this.syncLoading = true
            await syncDept({}).then(res => {
                $this.$message({
                    message: '刷新成功',
                    type: 'success'
                })
            }).catch(function (err) {
                $this.$message.warning(err)
            })
            this.syncLoading = false
        },
        async syncKqGroup() {
            const $this = this
            this.syncLoading = true
            await syncKqGroup({}).then(res => {
                $this.$message({
                    message: '刷新成功',
                    type: 'success'
                })
            }).catch(function (err) {
                $this.$message.warning(err)
            })
            this.syncLoading = false
        },
        async syncUser() {
            const $this = this
            this.syncLoading = true
            await syncUser({}).then(res => {
                $this.$message({
                    message: '刷新成功',
                    type: 'success'
                })
            }).catch(function (err) {
                $this.$message({
                    message: err,
                    type: 'warning'
                })
            })
            this.syncLoading = false
        }
    }
}
</script>
<style scoped>

</style>
