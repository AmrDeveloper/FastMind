package com.amrdeveloper.fastmind.objects;

import com.amrdeveloper.fastmind.R;

public class Avatar {

    public static final Integer[] AVATARS = {
            R.drawable.ic_avatar0,
            R.drawable.ic_avatar1,
            R.drawable.ic_avatar2,
            R.drawable.ic_avatar3,
            R.drawable.ic_avatar4,
            R.drawable.ic_avatar5,
            R.drawable.ic_avatar6,
            R.drawable.ic_avatar7,
            R.drawable.ic_avatar8,
            R.drawable.ic_avatar9,
            R.drawable.ic_avatar10,
            R.drawable.ic_avatar12,
            R.drawable.ic_avatar15,
            R.drawable.ic_avatar16,
            R.drawable.ic_avatar17,
            R.drawable.ic_avatar18,
    };

    private int avatarId;

    public Avatar(int id) {
        this.avatarId = (id >= AVATARS.length) ? 0 : id;
    }

    public int getAvatarIndex() {
        return avatarId;
    }

    public int getAvatarId() {
        return AVATARS[avatarId];
    }
}
