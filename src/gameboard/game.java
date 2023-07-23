package gameboard;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

//0: khong bom, -1: co bom, -2: chua biet, 1->8: so bom lan can
public class game {
    private boolean isFirst = true;
    private boolean isLost = false;
    private boolean isEnded = false;

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean isEnded) {
        this.isEnded = isEnded;
    }


    private int xSize, ySize, mineCount;
    private int[][] mineMtx; //Ma tran bom

    private int[][] gameMtx; //Ma tran hien thi cho nguoi choi

    public int getMinesNumberForPos(int x, int y) {
        return gameMtx[x][y];
    }

    public game(int xSize, int ySize, int mineCount) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.mineCount = mineCount;
        this.mineMtx = new int[xSize][ySize];
        this.gameMtx = new int[xSize][ySize];

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                this.gameMtx[i][j] = -2;
                this.mineMtx[i][j] = 0;
            }
        }
    }

    private boolean spawnMine(int chance) //tao bom ngau nhien theo ti le (chance)
    {
        Random rand = new Random();
        int res = rand.nextInt(100);

        if (res <= chance)
            return true;

        return false;
    }

    private boolean isInSafeZone(int x, int y, int xSafe, int ySafe) {
        int[] xSurr = { 0, 1, 0, -1, 1, -1, -1, 1 };
        int[] ySurr = { 1, 0, -1, 0, 1, -1, 1, -1 };

        if(x == xSafe && y == ySafe)
            return true;
        else
        {
            for (int i = 0; i < xSurr.length; i++) {
                if(x == xSafe + xSurr[i] && y == ySafe + ySurr[i])
                    return true;
            }
        }

        return false;
    }

    private void initMine(int xEx, int yEx, int mineCount) //tao n bom trong ma tran bom
    {
        int count = 0;

        while (count < mineCount) {
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    if ( !isInSafeZone(i, j, xEx, yEx) //noi nguoi choi bat dau va cac noi lan can se khong co bom
                    && count < mineCount && mineMtx[i][j] != -1) {
                        if(spawnMine(12)) //cac noi con lai duyet qua co 12% la bom
                            mineMtx[i][j] = -1;

                        if (mineMtx[i][j] == -1)
                            count++;
                    }

                    if (count >= mineCount)
                        break;
                }
            }
        } //sau khi duyet qua ma tran neu van chua du so bom tao ra so voi quy dinh se quay lai tu dau ma tran
    }
    
    private boolean checkXY(int x, int y) //kiem tra vi tri hop le trong ma tran
    {
        if (x >= xSize || x < 0 || y >= ySize || y < 0)
            return false;
            
        return true;
    }
    
    private int countSurroundingMines(int x, int y) //dem so bom lan can
    {
        int[] xSurr = { 0, 1, 0, -1, 1, -1, -1, 1};
        int[] ySurr = { 1, 0, -1, 0, 1, -1, 1, -1};
        int count = 0;

        for (int i = 0; i < xSurr.length; i++) {
            if(checkXY(x + xSurr[i], y + ySurr[i]))
                if (mineMtx[x + xSurr[i]][y + ySurr[i]] == -1)
                    count++;
        }

        return count;
    }

    private void revealCell(int x, int y) //mo vi tri x, y
    {
        if (mineMtx[x][y] == 0)
            gameMtx[x][y] = countSurroundingMines(x, y);
        else
            gameMtx[x][y] = -1;
    }
    
    public boolean revealGroup(int x, int y) { //mo lien tiep cac noi khong co bom, true neu vi tri mo ban dau la bom nguoc lai false
        if (isFirst) // khoi tao ma tran bom vao o dau tien duoc mo de nguoi choi khong an bom khi moi bat dau
        {
            initMine(x, y, mineCount);
            isFirst = false;
        }

        revealCell(x, y);

        if (gameMtx[x][y] == -1)
            return true;

        if (gameMtx[x][y] == 0) {
            int[] xSurr = { 0, 1, 0, -1, 1, -1, -1, 1 };
            int[] ySurr = { 1, 0, -1, 0, 1, -1, 1, -1 };

            Stack<int[]> cells = new Stack<>();

            for (int i = 0; i < xSurr.length; i++) {
                if (checkXY(x + xSurr[i], y + ySurr[i]))
                    if (mineMtx[x + xSurr[i]][y + ySurr[i]] == 0 && gameMtx[x + xSurr[i]][y + ySurr[i]] == -2)
                        cells.push(new int[] { x + xSurr[i], y + ySurr[i] });
            }

            while (!cells.empty()) {
                int[] cell = cells.pop();

                revealCell(cell[0], cell[1]);

                if (gameMtx[cell[0]][cell[1]] == 0) {
                    for (int i = 0; i < xSurr.length; i++) {
                        if (checkXY(cell[0] + xSurr[i], cell[1] + ySurr[i]))
                            if (mineMtx[cell[0] + xSurr[i]][cell[1] + ySurr[i]] == 0
                                    && gameMtx[cell[0] + xSurr[i]][cell[1] + ySurr[i]] == -2)
                                cells.push(new int[] { cell[0] + xSurr[i], cell[1] + ySurr[i] });
                    }
                }
            }
        }

        return false;
    }
    
    public int revealWithResult(int x, int y) {
        isLost = revealGroup(x, y);

        return gameState();
    }

    private int countGameCells() //dem cac o nguoi choi chua mo
    {
        int count = 0;
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                if (gameMtx[i][j] == -2)
                    count++;
            }
        }

        return count;
    }
    
    public int gameState() { //1: thang, -1: thua, 0: chua xac dinh
        if (isLost) { // mo trung bom -> Lose
            setEnded(true);
            isLost = true;
            return -1;
        }
        
        if (countGameCells() == mineCount){ // so o chua mo = so bom -> Win
            setEnded(true);
            return 1;
        }

        return 0;
    }
}
