package com.rodyandroid.clasesparticulares;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.rodyandroid.clasesparticulares.Interfaces.OnItemDoubleClickListener;
import com.rodyandroid.clasesparticulares.Model.Profesor;

import java.util.List;

public class ListaProfesorAdapter extends RecyclerView.Adapter<ListaProfesorAdapter.ViewHolder> {
    private List<Profesor> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListener mListener;

    public ListaProfesorAdapter(List<Profesor> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, parent, false);
        return new ViewHolder(view, doubleClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profesor item = mData.get(position);
        holder.bindData(item);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION && clickedPosition < mData.size()) {

                    if (mListener != null) {
                        mListener.onItemClick(mData.get(clickedPosition));
                    }
                }
            }
        });

        // Prueba eliminarfavoritos


        if (holder.imageButtonEliminar != null) {
            holder.imageButtonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION && clickedPosition < mData.size()) {
                        // Llamar al método para eliminar el profesor de la lista
                        eliminarProfesor(clickedPosition);

                        // Notificar al usuario que el profesor fue eliminado
                        Toast.makeText(mContext, "Profesor eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //Fin de prueba eliminarfavoritos

        holder.imageButtonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION && clickedPosition < mData.size()) {
                    // Verificar si el listener de favoritos no es nulo y la posición es válida
                    if (mFavoritosListener != null) {
                        mFavoritosListener.onFavoritosClick(mData.get(clickedPosition));
                    }
                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Obtener el profesor correspondiente a esta posición
                Profesor profesor = mData.get(holder.getAdapterPosition());

                // Crear un Intent para abrir la actividad del perfil del profesor
                Intent intent = new Intent(mContext, PerfilProfesorActivity.class);

                // Pasar información adicional sobre el profesor si es necesario
                intent.putExtra("profesor_id", profesor.getId());
                intent.putExtra("profesor_nombre", profesor.getNombre());


                // Iniciar la actividad del perfil del profesor
                mContext.startActivity(intent);


                return true;
            }
        });
    }

    // Método para actualizar la lista de profesores
    public void setProfesores(List<Profesor> profesores) {
        mData = profesores;
        notifyDataSetChanged();
    }

    // Método para eliminar un profesor de la lista
    public void eliminarProfesor(int position) {
        if (position >= 0 && position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnProfesorFavoritoClickListener(ListaProfeActivity listaProfeActivity) {

    }

    public interface OnItemDoubleClickListener {
        void onItemDoubleClick(Profesor profesor);
    }


    public interface OnItemClickListener {
        void onItemClick(Profesor profesor);
    }

    public interface OnFavoritosClickListener {
        void onFavoritosClick(Profesor profesor);
    }

    private OnFavoritosClickListener mFavoritosListener;

    public void setOnFavoritosClickListener(OnFavoritosClickListener listener) {
        this.mFavoritosListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {
        ImageView iconImageView;
        TextView nombre, ubicacion, asignatura, contacto;
        ImageButton imageButtonFavoritos, imageButtonEliminar;
        private GestureDetector gestureDetector;
        private OnItemDoubleClickListener doubleClickListener;


        ViewHolder(View itemView, OnItemDoubleClickListener listener) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nombre = itemView.findViewById(R.id.nombreTextView);
            ubicacion = itemView.findViewById(R.id.ubicacionTextView);
            asignatura = itemView.findViewById(R.id.asignaturaTexView);
            contacto = itemView.findViewById(R.id.contactoTextView);

            this.doubleClickListener = listener;
            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Profesor profesor = mData.get(position);
                        doubleClickListener.onItemDoubleClick(profesor);
                        return true;
                    }
                    return super.onDoubleTap(e);
                }
            });
            itemView.setOnTouchListener(this);

            //Prueba eliminarFavoritos
            imageButtonFavoritos = itemView.findViewById(R.id.imageButtonFavoritos);
            imageButtonEliminar = itemView.findViewById(R.id.imageButFavEliminar);

            if (imageButtonEliminar != null) {
                imageButtonEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int clickedPosition = getAdapterPosition();
                        if (clickedPosition != RecyclerView.NO_POSITION && clickedPosition < mData.size()) {
                            eliminarProfesor(clickedPosition);
                            Toast.makeText(mContext, "Profesor eliminado de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            //Fin de prueba eliminarfavoritos

        }

        void bindData(final Profesor item) {
            // Bind data to views
            nombre.setText(item.getNombre());
            ubicacion.setText(item.getUbicacion());
            asignatura.setText(item.getAsignaturas());
            contacto.setText(item.getTelefono());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }


        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }


        public boolean onDoubleTap(MotionEvent e) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Profesor profesor = mData.get(position);
                if (doubleClickListener != null) {
                    Toast.makeText(mContext, "Doble clic detectado", Toast.LENGTH_SHORT).show();
                    doubleClickListener.onItemDoubleClick(profesor);
                }
            }
            return true;
        }


        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(@NonNull MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {

        }

        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private OnItemDoubleClickListener doubleClickListener;

    public void setOnItemDoubleClickListener(OnItemDoubleClickListener listener) {
        this.doubleClickListener = listener;
    }

}
