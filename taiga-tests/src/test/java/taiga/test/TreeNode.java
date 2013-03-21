package taiga.test;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	private List<TreeNode> children = new ArrayList<TreeNode>();

	public List<TreeNode> getChildren() {
		return children;
	}
	
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
}
