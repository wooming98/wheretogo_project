package com.backend.mapper.post;

import com.backend.domain.post.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    // 게시글 추가 매퍼
    @Insert("""
            INSERT INTO post (title, content, memberid)
            VALUES (#{title}, #{content}, #{memberId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "postId")
    int insert(Post post);

    // 게시글 조회 매퍼
    @Select("""
            SELECT p.postid, p.title, p.content, p.createdate, p.view, m.nickname, p.memberid
            FROM post p JOIN member m
            ON p.memberid = m.memberid
            WHERE p.postid = #{postId}
            """)
    Post selectById(Integer postId);

    // 게시글 리스트 매퍼
    @Select("""
            <script>
            SELECT p.postid, p.title, p.content, m.nickname
            FROM post p JOIN member m ON p.memberid = m.memberid
            <where>
                <if test="searchType != null">
                    <bind name="pattern" value="'%' + searchKeyword + '%'"/>
                    <if test="searchType =='all' || searchType =='title'">
                        OR p.title LIKE #{pattern}
                        OR p.content LIKE #{pattern}
                    </if>
                    <if test="searchType == 'all' || searchType == 'nickName'">
                        OR m.nickname LIKE #{pattern}
                    </if>
                </if>
            </where>
            GROUP BY p.postid
            ORDER BY p.postid DESC
            LIMIT #{offset}, 5
            </script>
            """)
    List<Post> selectAllPost(Integer offset, String searchType, String searchKeyword);

    // 게시글 리스트 카운트 매퍼
    @Select("""
            <script>
            SELECT COUNT(p.postid)
            FROM post p JOIN member m ON p.memberid = m.memberid
                <where>
                    <if test="searchType != null">
                        <bind name="pattern" value="'%' + searchKeyword + '%'"/>
                        <if test="searchType =='all' || searchType =='title'">
                            OR p.title LIKE #{pattern}
                            OR p.content LIKE #{pattern}
                        </if>
                        <if test="searchType == 'all' || searchType == 'nickName'">
                            OR m.nickname LIKE #{pattern}
                        </if>
                    </if>
                </where>
            </script>
            """)
    Integer countAllPost(String searchType, String searchKeyword);

    // 게시글 수정 매퍼
    @Update("""
            UPDATE post
            SET title=#{title}, content=#{content}
            WHERE postid=#{postId}
            """)
    void update(Post post);

    // 게시글 삭제 매퍼
    @Delete("""
            DELETE FROM post
            WHERE postid=#{postId}
            """)
    Integer deleteById(Integer postId);
}
