package com.example.chatproject.Adapter;

public class RequestRecyclerAdapter extends FirestoreRecyclerAdapter<Request, RequestRecyclerAdapter.RequestViewHolder> {

    private DatabaseRepository databaseRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private GetTimeAgo getTimeAgo;
    private onButtonClickListener listener;

    @Inject
    public RequestRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Request> options,DatabaseRepository databaseRepository,GetTimeAgo getTimeAgo) {
        super(options);
        this.databaseRepository = databaseRepository;
        this.getTimeAgo = getTimeAgo;
    }

    public void setOnClickListener(onButtonClickListener listener){
        this.listener = listener;
    }

    public void clearDisposable(){
        if(disposable != null){
            disposable.clear();
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull final Request model) {
        holder.timeView.setText(getTimeAgo.getTimeAgo(Long.parseLong(model.getTimeStamp())));
        holder.requestTextView.setText("You have new friend request !");
        databaseRepository.getUserinfo(getSnapshots().getSnapshot(position).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<DocumentSnapshot>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        holder.displayName.setText(user.getDisplayName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.request_view_item,parent,false);
        return new RequestViewHolder(view);
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView displayName;
        TextView timeView;
        TextView requestTextView;
        Button acceptBtn;
        Button declineBtn;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.display_name);
            timeView = itemView.findViewById(R.id.time);
            requestTextView = itemView.findViewById(R.id.request_text);
            acceptBtn = itemView.findViewById(R.id.accept_btn);
            declineBtn = itemView.findViewById(R.id.decline_btn);

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAcceptAction(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                }
            });

            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeclineAction(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                }
            });
        }
    }

    public interface onButtonClickListener{
        void onAcceptAction(String uid);
        void onDeclineAction(String uid);
    }
}
