package com.codestates.tag.entity;

import com.codestates.reviewBoard.entity.ReviewBoardTag;
import com.codestates.user.entity.UserTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String tagName;

    @OneToMany
    private ReviewBoardTag reviewBoardTag;

    @OneToMany
    private UserTag userTag;
}