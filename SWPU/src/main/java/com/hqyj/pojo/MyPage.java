package com.hqyj.pojo;

/*描述分页对象*/

public class MyPage {
    //定义页面，并赋值1，表示第一页
    private int page=1;

    //定义每一页显示的数据条数,并赋值2，表示每页显示两条数据
    private int row=2;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
