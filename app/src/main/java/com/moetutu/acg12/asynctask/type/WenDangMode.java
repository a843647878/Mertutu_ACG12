package com.moetutu.acg12.asynctask.type;

import java.util.List;

/**
 * Created by chengwanying on 16/5/30.
 */
public class WenDangMode {

    private String status;


    private PostBean post;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PostBean getPost() {
        return post;
    }

    public void setPost(PostBean post) {
        this.post = post;
    }

    public static class PostBean {
        private int ID;

        private PostAuthorBean post_author;
        private String post_date;
        private String post_date_gmt;
        private String post_content;
        private String post_title;
        private String post_excerpt;
        private String post_status;
        private String comment_status;
        private String ping_status;
        private String post_password;
        private String post_name;
        private String to_ping;
        private String pinged;
        private String post_modified;
        private String post_modified_gmt;
        private String post_content_filtered;
        private int post_parent;
        private String guid;
        private int menu_order;
        private String post_type;
        private String post_mime_type;
        private String comment_count;
        private String filter;
        private String url;


        private ThumbnailBean thumbnail;
        private String download_page;

        private List<PostCategoriesBean> post_categories;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public PostAuthorBean getPost_author() {
            return post_author;
        }

        public void setPost_author(PostAuthorBean post_author) {
            this.post_author = post_author;
        }

        public String getPost_date() {
            return post_date;
        }

        public void setPost_date(String post_date) {
            this.post_date = post_date;
        }

        public String getPost_date_gmt() {
            return post_date_gmt;
        }

        public void setPost_date_gmt(String post_date_gmt) {
            this.post_date_gmt = post_date_gmt;
        }

        public String getPost_content() {
            return post_content;
        }

        public void setPost_content(String post_content) {
            this.post_content = post_content;
        }

        public String getPost_title() {
            return post_title;
        }

        public void setPost_title(String post_title) {
            this.post_title = post_title;
        }

        public String getPost_excerpt() {
            return post_excerpt;
        }

        public void setPost_excerpt(String post_excerpt) {
            this.post_excerpt = post_excerpt;
        }

        public String getPost_status() {
            return post_status;
        }

        public void setPost_status(String post_status) {
            this.post_status = post_status;
        }

        public String getComment_status() {
            return comment_status;
        }

        public void setComment_status(String comment_status) {
            this.comment_status = comment_status;
        }

        public String getPing_status() {
            return ping_status;
        }

        public void setPing_status(String ping_status) {
            this.ping_status = ping_status;
        }

        public String getPost_password() {
            return post_password;
        }

        public void setPost_password(String post_password) {
            this.post_password = post_password;
        }

        public String getPost_name() {
            return post_name;
        }

        public void setPost_name(String post_name) {
            this.post_name = post_name;
        }

        public String getTo_ping() {
            return to_ping;
        }

        public void setTo_ping(String to_ping) {
            this.to_ping = to_ping;
        }

        public String getPinged() {
            return pinged;
        }

        public void setPinged(String pinged) {
            this.pinged = pinged;
        }

        public String getPost_modified() {
            return post_modified;
        }

        public void setPost_modified(String post_modified) {
            this.post_modified = post_modified;
        }

        public String getPost_modified_gmt() {
            return post_modified_gmt;
        }

        public void setPost_modified_gmt(String post_modified_gmt) {
            this.post_modified_gmt = post_modified_gmt;
        }

        public String getPost_content_filtered() {
            return post_content_filtered;
        }

        public void setPost_content_filtered(String post_content_filtered) {
            this.post_content_filtered = post_content_filtered;
        }

        public int getPost_parent() {
            return post_parent;
        }

        public void setPost_parent(int post_parent) {
            this.post_parent = post_parent;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public int getMenu_order() {
            return menu_order;
        }

        public void setMenu_order(int menu_order) {
            this.menu_order = menu_order;
        }

        public String getPost_type() {
            return post_type;
        }

        public void setPost_type(String post_type) {
            this.post_type = post_type;
        }

        public String getPost_mime_type() {
            return post_mime_type;
        }

        public void setPost_mime_type(String post_mime_type) {
            this.post_mime_type = post_mime_type;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ThumbnailBean getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(ThumbnailBean thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getDownload_page() {
            return download_page;
        }

        public void setDownload_page(String download_page) {
            this.download_page = download_page;
        }

        public List<PostCategoriesBean> getPost_categories() {
            return post_categories;
        }

        public void setPost_categories(List<PostCategoriesBean> post_categories) {
            this.post_categories = post_categories;
        }

        public static class PostAuthorBean {
            private String name;
            private String avatar;
            private String description;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class ThumbnailBean {
            private String thumbnail;
            private String medium;

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }

        public static class PostCategoriesBean {
            private int term_id;
            private String name;
            private String slug;
            private int term_group;
            private int term_taxonomy_id;
            private String taxonomy;
            private String description;
            private int parent;
            private int count;
            private String filter;
            private int cat_ID;
            private int category_count;
            private String category_description;
            private String cat_name;
            private String category_nicename;
            private int category_parent;
            private String url;

            public int getTerm_id() {
                return term_id;
            }

            public void setTerm_id(int term_id) {
                this.term_id = term_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public int getTerm_group() {
                return term_group;
            }

            public void setTerm_group(int term_group) {
                this.term_group = term_group;
            }

            public int getTerm_taxonomy_id() {
                return term_taxonomy_id;
            }

            public void setTerm_taxonomy_id(int term_taxonomy_id) {
                this.term_taxonomy_id = term_taxonomy_id;
            }

            public String getTaxonomy() {
                return taxonomy;
            }

            public void setTaxonomy(String taxonomy) {
                this.taxonomy = taxonomy;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getParent() {
                return parent;
            }

            public void setParent(int parent) {
                this.parent = parent;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getFilter() {
                return filter;
            }

            public void setFilter(String filter) {
                this.filter = filter;
            }

            public int getCat_ID() {
                return cat_ID;
            }

            public void setCat_ID(int cat_ID) {
                this.cat_ID = cat_ID;
            }

            public int getCategory_count() {
                return category_count;
            }

            public void setCategory_count(int category_count) {
                this.category_count = category_count;
            }

            public String getCategory_description() {
                return category_description;
            }

            public void setCategory_description(String category_description) {
                this.category_description = category_description;
            }

            public String getCat_name() {
                return cat_name;
            }

            public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
            }

            public String getCategory_nicename() {
                return category_nicename;
            }

            public void setCategory_nicename(String category_nicename) {
                this.category_nicename = category_nicename;
            }

            public int getCategory_parent() {
                return category_parent;
            }

            public void setCategory_parent(int category_parent) {
                this.category_parent = category_parent;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
