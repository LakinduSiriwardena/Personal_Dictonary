import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
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
            System.out.println("2. Check word definition or delete a word");
            System.out.println("3. View all words in alphabetical order");
            System.out.println("4. View words by entry time");
            System.out.println("5. Exit");

            System.out.println("Enter choice :");


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
                    viewAllWords();
                    break;
                case 4:
                    viewAllWordsByTimestamp();
                    break;

                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }
        scanner.close();
    }
        private void viewAllWordsByTimestamp() {
            System.out.println("All words based on timestamp:");
            dictionary.printWordsByTimestamp();
        }

        private void viewAllWords() {
            System.out.println("All words in alphabetical order:");
            dictionary.inOrderTraversal();
        }

    private void addNewWord() {
        System.out.print("Enter the word: ");
        String word = scanner.nextLine();

        String existingDefinition = dictionary.search(word);
        if (existingDefinition != null) {
            System.out.println("Word '" + word + "' already exists in the dictionary with definition: " + existingDefinition);
            System.out.println("Do you want to edit the definition? (yes/no)");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("yes")) {
                editWordDefinition(word);
            }
            return; // Exit the method without adding the word
        }
        System.out.print("Enter the definition: ");
        String definition = scanner.nextLine();

        dictionary.insert(word, definition);
        System.out.println("Word '" + word + "' added to the dictionary.");
    }
        private void editWordDefinition(String word) {
            System.out.print("Enter the new definition: ");
            String newDefinition = scanner.nextLine();
            dictionary.insert(word, newDefinition);
            System.out.println("Definition of word '" + word + "' updated.");
        }

    private void checkWordDefinition() {
        System.out.print("Enter the word to check: ");
        String word = scanner.nextLine();

        String definition = dictionary.search(word);

        if (definition != null) {
            System.out.println("Definition of '" + word + "': " + definition);
            System.out.println("Do you want to delete this word? (yes/no)");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("yes")) {
                dictionary.delete(word);
                System.out.println("Word '" + word + "' deleted from the dictionary.");
            }
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
    long timestamp; // Timestamp for the word


    AVLNode(String word, String definition, long timestamp) {
        this.word = word;
        this.definition = definition;
        this.timestamp = timestamp;
        this.height = 1;
    }
}

 class AVLDictionary {
     private AVLNode root; // Declare 'root' variable

     private List<AVLNode> wordsByTimestamp; // List to store words along with their timestamps

     public AVLDictionary() {
         this.wordsByTimestamp = new ArrayList<>();
     }


     public void insert(String word, String definition) {
         long timestamp = System.currentTimeMillis(); // Current timestamp
         root = insertRec(root, word, definition, timestamp); // Pass 'root' here
         wordsByTimestamp.add(new AVLNode(word, definition, timestamp));
     }

     private AVLNode insertRec(AVLNode root, String word, String definition, long timestamp)  {
        if (root == null) {
            return new AVLNode(word, definition,timestamp);
        }

        if (word.compareTo(root.word) < 0) {
            root.left = insertRec(root.left, word, definition,timestamp);
        } else if (word.compareTo(root.word) > 0) {
            root.right = insertRec(root.right, word, definition,timestamp);
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
     public void inOrderTraversal() {
         inOrderTraversal(root);
     }


     // Recursive helper method for in-order traversal
     private void inOrderTraversal(AVLNode node) {
         if (node != null) {
             inOrderTraversal(node.left);
             System.out.println(node.word);
             inOrderTraversal(node.right);
         }
     }
     public void printWordsByTimestamp() {
         for (AVLNode node : wordsByTimestamp) {
             System.out.println(node.word + " (Timestamp: " + new Date(node.timestamp) + ")");
         }
     }



 }
