package com.microwu.cxd.mybatis;

/**
 * 动态 sql
 *
 * if
 *
 * <select id="findActiveBlogWithTitleLike"
 *      resultType="Blog">
 *   SELECT * FROM BLOG
 *   WHERE state = ‘ACTIVE’
 *   <if test="title != null">
 *     AND title like #{title}
 *   </if>
 * </select>
 *
 * 这条语句提供了可选的查找文本功能。如果不传 title，那么所有处于 ACTIVE 状态的 BLOG 都会返回；
 * 如果传入了 title 参数，那么就会对 title 一列进行模糊查找并返回对应的 BLOG 结果
 *
 * 如果希望 title 和 author 两个参数进行可选搜索？
 *
 * <select id="findActiveBlogLike"
 *      resultType="Blog">
 *   SELECT * FROM BLOG WHERE state = ‘ACTIVE’
 *   <if test="title != null">
 *     AND title like #{title}
 *   </if>
 *   <if test="author != null and author.name != null">
 *     AND author_name like #{author.name}
 *   </if>
 * </select>
 *
 * choose、when、otherwise
 *
 * 有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了
 * choose 元素，它有点像 Java 中的 switch 语句
 *
 * 策略变为：传入了 title 就按 title 查找，传入了 author 就按 author 查找。
 * 若两者都没有传入，就返回标记为 featured 的 BLOG
 *
 * <select id="findActiveBlogLike"
 *      resultType="Blog">
 *   SELECT * FROM BLOG WHERE state = ‘ACTIVE’
 *   <choose>
 *     <when test="title != null">
 *       AND title like #{title}
 *     </when>
 *     <when test="author != null and author.name != null">
 *       AND author_name like #{author.name}
 *     </when>
 *     <otherwise>
 *       AND featured = 1
 *     </otherwise>
 *   </choose>
 * </select>
 *
 * trim、where、set
 *
 * 如果我们将 state = ACTIVE 设置成动态条件
 *
 * <select id="findActiveBlogLike"
 *      resultType="Blog">
 *   SELECT * FROM BLOG
 *   WHERE
 *   <if test="state != null">
 *     state = #{state}
 *   </if>
 *   <if test="title != null">
 *     AND title like #{title}
 *   </if>
 *   <if test="author != null and author.name != null">
 *     AND author_name like #{author.name}
 *   </if>
 * </select>
 *
 * 如果没有匹配条件:
 *
 * SELECT * FROM BLOG
 * WHERE
 *
 * 如果匹配的是第二个条件:
 *
 * SELECT * FROM BLOG
 * WHERE
 * AND title like ‘someTitle’
 *
 * <select id="findActiveBlogLike"
 *      resultType="Blog">
 *   SELECT * FROM BLOG
 *   <where>
 *     <if test="state != null">
 *          state = #{state}
 *     </if>
 *     <if test="title != null">
 *         AND title like #{title}
 *     </if>
 *     <if test="author != null and author.name != null">
 *         AND author_name like #{author.name}
 *     </if>
 *   </where>
 * </select>
 *
 * where 元素只会在子元素返回任何内容的情况下才插入 WHERE 字句。而且，若字句的开头为 AND 或
 * OR，where 元素也会将它们去除
 *
 * <trim prefix="WHERE" prefixOverrides="AND |OR ">
 *   ...
 * </trim>
 *
 * prefixOverrides 属性会忽略功过管道符分割的文本序列（注意此例中的空格是必要的）。上述例子会移除所
 * 有 prefixOverrides 属性中指定的内容，并且插入 prefix 属性中指定的内容
 *
 * <update id="updateAuthorIfNecessary">
 *   update Author
 *     <set>
 *       <if test="username != null">username=#{username},</if>
 *       <if test="password != null">password=#{password},</if>
 *       <if test="email != null">email=#{email},</if>
 *       <if test="bio != null">bio=#{bio}</if>
 *     </set>
 *   where id=#{id}
 * </update>
 *
 * set 元素会动态地在行首插入 SET 关键字，并会删除额外的逗号
 *
 * 与 set 元素等价的自定义 trim 元素:
 *
 * <trim prefix="SET" suffixOverrides=",">
 *   ...
 * </trim>
 *
 * foreach
 *
 * <select id="selectPostIn" resultType="domain.blog.Post">
 *   SELECT *
 *   FROM POST P
 *   WHERE ID in
 *   <foreach item="item" index="index" collection="list"
 *       open="(" separator="," close=")">
 *         #{item}
 *   </foreach>
 * </select>
 *
 * foreach 允许你指定一个集合，声明可以在元素体内使用的集合项 item 和索引
 * index 变量。它也允许你指定开头与结尾的字符串以及集合项迭代之间的分隔符。
 *
 * index 是当前迭代的序号，item 的值是本次迭代获取到的元素。当使用 Map 对象时，
 * index 是键，item 是值
 *
 * 注意，我们覆盖了后缀值设置，并自定义了前缀值
 *
 * https://mybatis.org/mybatis-3/zh/dynamic-sql.html
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/28  10:58
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class DynamicSqlDemo {
}
