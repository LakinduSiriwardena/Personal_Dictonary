import java.util.Scanner;
public class Main {

        public static void main(String[] args) {
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        }
    }

    class ConsoleUI {
    private AVLDictionary dictionary;
    private Scanner scanner;

    public ConsoleUI() {
        this.dictionary = new AVLDictionary();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("Select an option:");
            System.out.println("1. Add new word");
            System.out.println("2. Check word definition");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addNewWord();
                    break;
                case 2:
                    checkWordDefinition();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void addNewWord() {
        System.out.print("Enter the word: ");
        String word = scanner.nextLine();

        System.out.print("Enter the definition: ");
        String definition = scanner.nextLine();

        dictionary.insert(word, definition);
        System.out.println("Word '" + word + "' added to the dictionary.");
    }

    private void checkWordDefinition() {
        System.out.print("Enter the word to check: ");
        String word = scanner.nextLine();

        String definition = dictionary.search(word);
        if (definition != null) {
            System.out.println("Definition of '" + word + "': " + definition);
        } else {
            System.out.println("Word '" + word + "' not found in the dictionary.");
        }
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}

class AVLNode {
    String word;
    String definition;
    AVLNode left;
    AVLNode right;
    int height;

    AVLNode(String word, String definition) {
        this.word = word;
        this.definition = definition;
        this.height = 1;
    }
}

 class AVLDictionary {
    private AVLNode root;

    public void insert(String word, String definition) {
        root = insertRec(root, word, definition);
    }

    private AVLNode insertRec(AVLNode root, String word, String definition) {
        if (root == null) {
            return new AVLNode(word, definition);
        }

        if (word.compareTo(root.word) < 0) {
            root.left = insertRec(root.left, word, definition);
        } else if (word.compareTo(root.word) > 0) {
            root.right = insertRec(root.right, word, definition);
        } else {
            root.definition = definition;
            return root;
        }

        root.height = 1 + Math.max(height(root.left), height(root.right));

        int balance = getBalance(root);

        if (balance > 1 && word.compareTo(root.left.word) < 0) {
            return rightRotate(root);
        }
        if (balance < -1 && word.compareTo(root.right.word) > 0) {
            return leftRotate(root);
        }
        if (balance > 1 && word.compareTo(root.left.word) > 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && word.compareTo(root.right.word) < 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    public void delete(String word) {
        root = deleteRec(root, word);
    }

    private AVLNode deleteRec(AVLNode root, String word) {
        if (root == null) {
            return root;
        }

        if (word.compareTo(root.word) < 0) {
            root.left = deleteRec(root.left, word);
        } else if (word.compareTo(root.word) > 0) {
            root.right = deleteRec(root.right, word);
        } else {
            if (root.left == null || root.right == null) {
                AVLNode temp = root.left != null ? root.left : root.right;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                AVLNode successor = minValueNode(root.right);
                root.word = successor.word;
                root.definition = successor.definition;
                root.right = deleteRec(root.right, successor.word);
            }
        }

        if (root == null) {
            return root;
        }

        root.height = 1 + Math.max(height(root.left), height(root.right));

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    public String search(String word) {
        return searchRec(root, word);
    }

    private String searchRec(AVLNode root, String word) {
        if (root == null) {
            return null;
        }
        if (word.equals(root.word)) {
            return root.definition;
        } else if (word.compareTo(root.word) < 0) {
            return searchRec(root.left, word);
        } else {
            return searchRec(root.right, word);
        }
    }

    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
}
