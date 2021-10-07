

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {


    private val viewModel : GameViewModel by viewModels()


    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment
    //game_fragment 레이아웃 xml 확장
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = GameFragmentBinding.inflate(inflater, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d("GameFragment", "Word: ${viewModel.wordtest} ScrambleWord: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
        // Inflate the layout XML file and return a binding object instance
        return binding.root
    }

    //버튼클릭 리스너 설정, ui 업데이트
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI
        updateNextWordOnScreen()
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
                R.string.word_count, 0, MAX_NO_OF_WORDS)
    }


    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    //submit 버튼 클릭 리스너
    //뒤섞인 단어 표시, 텍스트필드 지우기, 플레이어의단어검증x, 점수와 단어수 증가
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) { //단어맞췄을때
            setErrorTextField(false)
            if (viewModel.nextWord()) { //게임안끝났을때
                updateNextWordOnScreen()
            } else { //게임끝낫을때
                showFinalScoreDialog() 
            }
        }
        else { //단어 못맞췄을때
            setErrorTextField(true)
        }
//        currentScrambledWord = getNextScrambledWord()
//        currentWordCount++
//        score += SCORE_INCREASE
//        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
//        binding.score.text = getString(R.string.score, score)
//        setErrorTextField(false)
//        updateNextWordOnScreen()
    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    //skip 버튼 클릭 리스너 : 점수를 제외하고 onSubmitWord와 유사하게 ui 업데이트
    private fun onSkipWord() {
        if(viewModel.nextWord()){
            setErrorTextField(false)
            updateNextWordOnScreen()
        }
        else {
            showFinalScoreDialog()
        }
//        currentScrambledWord = getNextScrambledWord()
//        currentWordCount++
//        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
//        setErrorTextField(false)
//        updateNextWordOnScreen()
    }

    // Gets a random word for the list of words and shuffles the letters in it.
    // 단어목록에서 임의 단어 선택 후 글자를 섞음
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }


    private fun showFinalScoreDialog(){
        /*
        * context : 애플리케이션, 활동, 프래그먼트의 컨텍스트나 현재 상태를 의미
        * 일반적으로 리소스, 데이터베이스, 기타 시스템 서비스에 액세스하는 데 사용
        */
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score))
            .setCancelable(false) //뒤로버튼눌러서 취소x
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }


    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        viewModel.reinitalizeData()
        setErrorTextField(false)
        updateNextWordOnScreen()
    }

    // Exits the game.
    private fun exitGame() {
        activity?.finish()
    }

    //활동, 프래그먼트 소멸될 때 호출출
    override fun onDetach() {
        super.onDetach()
        Log.d("GameFragment", "GameFragment destroyed!")
    }

    /*
    * Sets and resets the text field error status.
    */
    //글자가 뒤섞인 새로운 단어 표시
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

    /*
     * Displays the next scrambled word on screen.
     */
    private fun updateNextWordOnScreen() {
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord

//        binding.score.text = viewModel.score.toString()
//        binding.wordCount.text = viewModel.currentWordCount.toString()
    }


}
