
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.response.PositionItemDTO
import com.lab42.skillsclub.databinding.ItemPositionBinding

class PositionAdapter(private val dataSet: List<PositionItemDTO>) :
    RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    private var selectedPosition: Int = -1
    private var selectedPositionName: String = ""

    private var selectedItemPos: Int = -1
    private var previousSelected: Int = -1

    fun setSelectedPosition(position: Int) {
        previousSelected = selectedItemPos
        selectedItemPos = position
        selectedPosition = dataSet[position].id
        selectedPositionName = dataSet[position].name

        notifyItemChanged(previousSelected)
        notifyItemChanged(selectedItemPos)
    }


    class PositionViewHolder(private val binding: ItemPositionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PositionItemDTO, isSelected: Boolean) {
            binding.positionText.text = item.name

            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.sh_frame_position_active)
            } else {
                binding.root.setBackgroundResource(R.drawable.sh_frame_position)
            }
        }
    }

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    fun getSelectedPositionName(): String {
        return selectedPositionName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPositionBinding.inflate(inflater, parent, false)
        return PositionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PositionViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val isSelected = position == selectedItemPos
        holder.bind(dataSet[position], isSelected)

        holder.itemView.setOnClickListener {
            setSelectedPosition(position)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}
