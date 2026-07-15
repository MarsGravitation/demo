package main

/*
https://mp.weixin.qq.com/s/_4K-zDZgCPvSBmjHbj6GGA
*/
// func main() {

// }

/*
next[0] = 'a'
next[1] = 'b'
*/
type trieNode struct {
	// 子节点列表
	nexts [26]*trieNode
	// 通过当前节点的单词数量
	passCnt int
	// 标识是否存在单词以当前节点为结尾
	end bool
}

/*
前缀树的定义，trie root 本身对应的字符为空
*/
type Trie struct {
	root *trieNode
}

func NewTrie() *Trie {
	return &Trie{
		root: &trieNode{},
	}
}

// 查询目标节点，使得从更节点开始抵达目标节点沿字符形成的字符串恰好等于 word
func (t *Trie) Search(word string) bool {
	node := t.search(word)
	return node != nil && node.end
}

func (t *Trie) search(target string) *trieNode {
	// 移动指针从根节点触发
	move := t.root
	// 依次遍历 target 中的每个字符
	for _, ch := range target {
		// 倘若 next 中不存在这个字符的节点，说明该单词没插入过，返回 nil
		if move.nexts[ch-'a'] == nil {
			return nil
		}
		// 指针向着子节点移动
		move = move.nexts[ch-'a']
	}

	// 来到末尾，说明已经完全匹配好单词，直接返回这个节点
	// 需要注意，找到节点目标不一定代表单词存在，因为该节点的 end 标识未必为 ture
	return move
}

func (t *Trie) StartsWith(prefix string) bool {
	return t.search(prefix) != nil
}

func (t *Trie) Insert(word string) {
	if t.Search(word) {
		return
	}

	move := t.root

	for _, ch := range word {
		if move.nexts[ch-'a'] == nil {
			move.nexts[ch-'a'] = &trieNode{}
		}
		move.nexts[ch-'a'].passCnt++
		move = move.nexts[ch-'a']
	}

	move.end = true
}
