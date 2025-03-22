
import com.blink.snapgame.game.GameManager;
import com.blink.snapgame.match.MatchType;

import java.util.Scanner;

public class SnapGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Number of decks to use: ");
        int numDecks = scanner.nextInt();

        int matchBy;
        MatchType matchType = MatchType.BOTH;
        System.out.print("Match by");
        System.out.print("  1. for suit");
        System.out.print("  2. for value");
        System.out.print("  3. for both");
        do {
            matchBy = scanner.nextInt();
            switch (matchBy) {
                case 1:
                    matchType = MatchType.SUIT;
                    break;
                case 2:
                    matchType = MatchType.VALUE;
                    break;
                case 3:
                    break;
                default:
                    System.out.print("A number between 1 and 3 expected: ");
            }
        } while (matchBy < 1 || matchBy > 3);

        System.out.print("Number of players: ");
        int numPlayers = scanner.nextInt();

        System.out.print("Number of cards to stop the play (0 for until one player wins): ");
        int maxCardsToPlay = scanner.nextInt();
        scanner.close();

        GameManager gameManager = new GameManager(numDecks, numPlayers, matchType, maxCardsToPlay);
        gameManager.startGame();
    }
}