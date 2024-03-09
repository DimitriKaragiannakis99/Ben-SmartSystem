import React from 'react';

const HouseView = () => {
  return (
    <div className='flex flex-col h-full'>
      <h2 className='text-center my-4'>House View</h2>
      <div className='flex-grow relative'>
        <div className='absolute inset-0 m-4 bottom-20 border-2 border-dashed border-gray-400 flex justify-center items-center'>
          <span>...</span>
        </div>
      </div>
    </div>
  );
};

export default HouseView;
