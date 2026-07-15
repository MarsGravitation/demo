package com.cxd.hadoop.reducejoin;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Description:
 *                  Launched map tasks=2
 *                 Launched reduce tasks=1
 *                 Data-local map tasks=2
 *
 * order.txt
 *
 * id   pid amount
 * 1001	01	1
 * 1002	02	2
 * 1003	03	3
 * 1004	01	4
 * 1005	02	5
 * 1006	03	6
 *
 * pd.txt
 *
 * pid  amount
 * 01	小米
 * 02	华为
 * 03	格力
 *
 * 1004    小米    4
 * 1001    小米    1
 * 1005    华为    5
 * 1002    华为    2
 * 1006    格力    6
 * 1003    格力    3
 *
 * 分区 + 排序
 * 1001	01	1
 * 1004	01	4
 * 01	小米
 * 1002	02	2
 * 1005	02	5
 * 02	华为
 * 1003	03	3
 * 1006	03	6
 *
 * 分组 --- group by
 * 01 [1001	01	1, 1004	01	4, 01	小米]
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/6   14:46
 * Update History:
 * Author        Time            Content
 */
public class TableReducer extends Reducer<Text, TableBean, TableBean, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Reducer<Text, TableBean, TableBean, NullWritable>.Context context) throws IOException, InterruptedException {
        System.out.println("======================================================================================" + this);
        ArrayList<TableBean> orderBeans = new ArrayList<>();
        TableBean pdBean = new TableBean();

        for (TableBean value : values) {
            // 判断数据来自哪个表
            if ("order".equals(value.getFlag())) {
                // 订单表

                // 创建一个临时对象接收 value
                TableBean tmpOrderBean = new TableBean();
                copyProperties(tmpOrderBean, value);

                // 将临时 TableBean 添加到集合
                orderBeans.add(tmpOrderBean);
            } else {
                // 商品表
                copyProperties(pdBean, value);
            }
        }

        System.out.println("=========" + pdBean + ", " + orderBeans);

        // 遍历集合 orderBeans，替换掉每个 orderBean 的 pid 为 pname，然后写出
        for (TableBean orderBean : orderBeans) {
            orderBean.setPname(pdBean.getPname());

            // 写出修改后的 orderBean 对象
            context.write(orderBean, NullWritable.get());
        }
    }

    private void copyProperties(TableBean target, TableBean bean) {
        target.setId(bean.getId());
        target.setPid(bean.getPid());
        target.setAmount(bean.getAmount());
        target.setPname(bean.getPname());
        target.setFlag(bean.getFlag());
    }
}
