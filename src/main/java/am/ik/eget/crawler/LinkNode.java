package am.ik.eget.crawler;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import am.ik.eget.entity.Page;

public class LinkNode {
    private final int page;
    private final String path;
    private final LinkType type;
    private static final Pattern LINK_PAT = Pattern.compile(Pattern
            .quote("http://www.dmm.co.jp/monthly/")
            + "[a-z0-9]+"
            + Pattern.quote("/-/") + "([a-z]+)" + Pattern.quote("/"));
    private static final Pattern PAGE_PAT = Pattern.compile(Pattern
            .quote("/page=") + "([0-9]+)" + Pattern.quote("/"));

    public static enum LinkType {
        DETAIL, LIST, UNKNOWN
    }

    public LinkNode(String path) {
        this.path = path;
        Matcher m = LINK_PAT.matcher(path);
        if (m.find()) {
            String type = m.group(1);
            if ("detail".equals(type)) {
                this.type = LinkType.DETAIL;
                this.page = 0;
            } else {
                this.type = LinkType.LIST;
                Matcher p = PAGE_PAT.matcher(path);
                if (p.find()) {
                    this.page = Integer.valueOf(p.group(1));
                } else {
                    this.page = 1;
                }
            }
        } else {
            this.page = 0;
            this.type = LinkType.UNKNOWN;
        }
    }

    public int getPage() {
        return page;
    }

    public String getPath() {
        return path;
    }

    public LinkType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + page;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LinkNode other = (LinkNode) obj;
        if (page != other.page)
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    public Page toEntity() {
        Page page = new Page();
        page.setUrl(this.path);
        page.setExtracted(false);
        page.setPage(this.page);
        page.setType(this.type.name());
        page.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return page;
    }
    
    @Override
    public String toString() {
        return "LinkNode [path=" + path + ", type=" + type + ", page=" + page + "]";
    }

}
