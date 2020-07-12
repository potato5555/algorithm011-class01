class Solution {
   
        ArrayList<TreeNode> list = new ArrayList<>();
    public TreeNode lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {
        findlist(root,p);
        int lengthP = list.size();
        findlist(root,q);
        int k=0;
        for(int i=0;i<lengthP;i++){
            //若p为最近公共节点时,不会进行到break,会一直循环完毕;
            //前面一个判断条件是为了避免q所在的二叉树路径较短，且q为最近公共节点时，比如[1,2] 2;1;
            if(i+lengthP>=list.size()||list.get(i)!=list.get(i+lengthP)) break;
            k=i;//这里也可为k=i+lengthP;
        }
        return list.get(k);
    }
    //保存从根节点到目标节点的路径，包括目标节点，使用前序遍历,和寻找迷宫路径的思路较为相似
    public boolean findlist(TreeNode root,TreeNode target){
        if(root==null) return false;//这条路径无法找到target,显然null也不需要加入list中
        //这两句代码不可交换顺序，否则目标节点无法加入list
        list.add(root);
        if(root==target) return true;//找到了从根节点到target的路径
        //回溯过程
        //if(!(findlist(root.left,target)||findlist(root.right,target))){
        //下面这样的写法有一些剪枝的过程,更推荐
        //如果root.left和root.right都没有能找到target,则root也需要被删除
        if(!findlist(root.left,target)&&!findlist(root.right,target)) {
            list.remove(list.size()-1);//root为当前list的最后一个值,root.left和root.right在之前的迭代中已经进行删除了;
            return false;//显然此时没有找到到达target的路径，返回false;
        }
        return true;//找到路径后的回溯语句
    }
    
    //方法二：后序遍历，先左子树后右子树最后root,从底至顶回溯，代码简单，但是分析起来复杂;
    //当root==null时，说明当前路径没有找到p或者q
    //当root==p||root==q,假设先找到的是节点p:
    //则继续从找到的节点p开始寻找另一个节点q(迭代)，当第二个节点q也找到时,进行回溯,一开始回溯时，会一直保持单侧子树不为null的情况，但是注意这种情况，不是由于p,q均在当前单侧子树造成的，当前回溯过程的单侧子树内只有一个q;
    //继续回溯，当回溯至一个节点root时,节点 p,q在节点root的异侧,即双侧子树均不为null时,节点 root即为最近公共祖先。
    //返回root，后续则又会变为单侧子树不为null,这时才是因为p,q均在当前单侧子树造成的;
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //root=null,返回root说明这条路径没有找到p或者q;root=p||root=q时说明当前找到了p或者q;
    	if(root==null||root==p||root==q) return root;
        TreeNode left = lowestCommonAncestor(root.left,p,q);
        TreeNode right = lowestCommonAncestor(root.right,p,q);
        //后面是回溯过程,下面三个if语句可以交换顺序，如果固定顺序，则后面两个if可以进行简化
        //当root的左子树和右子树都没有p或者q时，root不是p和q祖先,返回null;
        if(left==null&&right==null) return null;
        
        //基于第一个if，这里的if可以简写为if(right==null) return left;
        //这里root的右子树没有找到p或者q,left找到了p或者q,这里返回的left有两种情况：
        //1.未找到最近公共节点时，即还在迭代阶段，这里返回的left为找到的p(假设为p),即第一个if的return结果：
        //若前面没有找到q,则从p开始继续进行迭代,在二叉树中剩余的节点中找到剩下的那个q;  若之前已经找到了q，则开始返回，直到找到left!=null&&right!=null，即找到了最近公共祖宗；
        //2.找到最近公共节点时，即处于返回阶段,不需要再进行迭代了,此时p,q在同一侧子树中,这里left为找到的最近公共节点,即下面最后一个return的结果;
        if(left!=null&&right==null) return left;
        //基于第一个if，这里的if可以简写为if(left==null) return right;
        //这里的分析和上面一个if类似;
        if(left==null&&right!=null) return right;
        return root;//即if(left!=null&&right!=null) return root;但注释中的写法会使得函数缺少return语句;
    }

    
}