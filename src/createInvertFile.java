import java.io.*;
import java.util.*;

public class createInvertFile {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        createInvertFile T = new createInvertFile();

        // 입력 파일 경로 받기
        System.out.print("input file 경로 입력: ");
        String inputFilePath = scanner.nextLine();

        // 출력 파일 경로 받기
        System.out.print("output file 경로 입력: ");
        String outputFilePath = scanner.nextLine();

        try {
            //파일 읽기
            Map<String, Map<Integer, Integer>> wordMap = fileReader(inputFilePath);
            //파일 쓰기
            fileWriter(wordMap, outputFilePath);

        } catch (IOException e) {
            System.out.println("입출력 오류입니다.");
        } catch (NumberFormatException e) {
            System.out.println("잘못된 문서 ID입니다.");
        }
    }

    //파일을 읽어오는 메서드
    public static Map<String, Map<Integer,Integer>> fileReader(String inputFilePath) throws IOException, NumberFormatException{

        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

        // 단어별로 문서ID와 빈도수 저장하는 맵
        //<단어,<문서ID, 빈도수>>
        Map<String, Map<Integer, Integer>> wordMap = new TreeMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            // 첫 공백을 기준으로 문서ID와 문장 나누기
            String[] parts = line.split(" ", 2);
            int docId = Integer.parseInt(parts[0]);
            // 소문자로 변환하고 특수기호를 제외한 단어를 배열에 저장
            String[] words = parts[1].toLowerCase().split("\\W+");

            for (String word : words) {
                if (!word.isEmpty()) { // 빈 문자열 무시
                    // 단어가 없으면 새로운 맵을 만들어줌
                    wordMap.putIfAbsent(word, new HashMap<>());
                    Map<Integer, Integer> docFrequencyMap = wordMap.get(word);

                    // 문서ID와 빈도수 업데이트
                    docFrequencyMap.put(docId, docFrequencyMap.getOrDefault(docId, 0) + 1);
                }
            }
        }

        reader.close();

        return wordMap;
    }



    //파일을 출력하는 메서드
    public static void fileWriter(Map<String,Map<Integer,Integer>> wordMap, String outputFilePath) throws IOException, NumberFormatException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        for (Map.Entry<String, Map<Integer, Integer>> entry : wordMap.entrySet()) {
            String word = entry.getKey();
            Map<Integer, Integer> docFrequencyMap = entry.getValue();

            // 빈도수를 기준으로 내림차순 정렬, 빈도수가 같으면 문서번호로 오름차순 정렬
            List<Map.Entry<Integer, Integer>> list = new ArrayList<>(docFrequencyMap.entrySet());
            list.sort((e1, e2) -> {
                int freqComparison = e2.getValue().compareTo(e1.getValue());
                return (freqComparison != 0) ? freqComparison : e1.getKey().compareTo(e2.getKey());
            });

            // 단어 출력 (한 번만)
            writer.write(word + " ");

            for (Map.Entry<Integer, Integer> docEntry : list) {
                int docId = docEntry.getKey();
                int frequency = docEntry.getValue();
                writer.write(docId + " " + frequency + " ");
            }
            writer.newLine();
        }

        // 파일 닫기
        writer.close();
    }
}
