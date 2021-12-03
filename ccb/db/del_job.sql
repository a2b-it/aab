delete  BATCH_STEP_EXECUTION_CONTEXT  where step_execution_id in (select step_execution_id from BATCH_STEP_EXECUTION where  job_execution_id=36)
delete BATCH_STEP_EXECUTION  where job_execution_id=36;
delete BATCH_JOB_EXECUTION_PARAMS where job_execution_id=36;
delete BATCH_JOB_EXECUTION_CONTEXT where job_execution_id=36;
delete BATCH_JOB_EXECUTION where job_execution_id=36;